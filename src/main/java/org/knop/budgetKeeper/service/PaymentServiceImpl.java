package org.knop.budgetKeeper.service;

import lombok.SneakyThrows;
import org.knop.budgetKeeper.dto.PaymentDto;
import org.knop.budgetKeeper.dto.PaymentsByCategoryStatsDto;
import org.knop.budgetKeeper.dto.PaymentsShortStatsDto;
import org.knop.budgetKeeper.models.*;
import org.knop.budgetKeeper.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Override
    public List<PaymentDto> getAllForUser(Integer userId) {
        return paymentRepository
                .findAllByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Payment::getDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(PaymentDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getAllForUserWithLimit(Integer userId, Integer limit) {
        return paymentRepository
                .findAllByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Payment::getDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .map(PaymentDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentsShortStatsDto> getShortPaymentsStats(Integer userId) {
        Date timeStart = Date.valueOf(LocalDate.now().withDayOfMonth(1));
        Date timeEnd = Date.valueOf(LocalDate.now());
        List<Payment> payments = paymentRepository.findAllByUserIdAndDateBetween(userId, timeStart, timeEnd);
        Map<Category, BigDecimal> categorySum = new HashMap<>();
        payments.stream()
                .filter(it -> !it.getIncomeLabel())
                .forEach(
                        it -> {
                            BigDecimal consumption = categorySum.get(it.getCategory());
                            if (consumption == null) {
                                consumption = it.getValue();
                            } else {
                                consumption = consumption.add(it.getValue());
                            }
                            categorySum.put(it.getCategory(), consumption);
                        }
                );

        return categorySum.entrySet()
                .stream()
                .map(it -> new PaymentsShortStatsDto(
                        it.getKey().getName(),
                        it.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentsByCategoryStatsDto getPaymentsStatsByPeriod(Integer userId, Date dateStart, Date dateEnd) {
        List<Payment> paymentsIncomeList = paymentRepository
                .findAllByUserIdAndIncomeLabelAndDateBetween(userId, true, dateStart, dateEnd);
        Map<Category, BigDecimal> categoryIncomeSum = new HashMap<>();
        paymentsIncomeList.stream()
                .forEach(
                        it -> {
                            BigDecimal income = categoryIncomeSum.get(it.getCategory());
                            if (income == null) {
                                income = it.getValue();
                            } else {
                                income = income.add(it.getValue());
                            }
                            categoryIncomeSum.put(it.getCategory(), income);
                        }
                );
        List<PaymentsShortStatsDto> incomeStatsListByCategory = categoryIncomeSum
                .entrySet()
                .stream()
                .map(it -> new PaymentsShortStatsDto(
                        it.getKey().getName(),
                        it.getValue()))
                .collect(Collectors.toList());

        List<Payment> paymentsExpenseList = paymentRepository
                .findAllByUserIdAndIncomeLabelAndDateBetween(userId, false, dateStart, dateEnd);
        Map<Category, BigDecimal> categoryExpenseSum = new HashMap<>();
        paymentsExpenseList.stream()
                .forEach(
                        it -> {
                            BigDecimal expense = categoryExpenseSum.get(it.getCategory());
                            if (expense == null) {
                                expense = it.getValue();
                            } else {
                                expense = expense.add(it.getValue());
                            }
                            categoryExpenseSum.put(it.getCategory(), expense);
                        }
                );
        List<PaymentsShortStatsDto> expenseStatsListByCategory = categoryExpenseSum
                .entrySet()
                .stream()
                .map(it -> new PaymentsShortStatsDto(
                        it.getKey().getName(),
                        it.getValue()))
                .collect(Collectors.toList());

        return new PaymentsByCategoryStatsDto(incomeStatsListByCategory, expenseStatsListByCategory);
    }

    @Override
    public List<PaymentsShortStatsDto> getStatsByCategory(Integer userId, Boolean incomeLabel, String categoryName, Date dateStart, Date dateEnd) {
        Optional<Category> category = categoryRepository
                .findByNameAndUserIdAndIncomeLabel(categoryName, userId, incomeLabel);
        if (!category.isPresent()) {
            return Collections.emptyList();
        }
        List<Subcategory> subcategory = subcategoryRepository
                .findAllByUserIdAndCategoryId(userId, category.get().getId());

        return subcategory.stream()
                .map(
                        it -> {
                            BigDecimal sum =
                                    paymentRepository.findAllByUserIdAndIncomeLabelAndSubcategoryIdAndDateBetween(
                                            userId,
                                            incomeLabel,
                                            it.getId(),
                                            dateStart,
                                            dateEnd
                                    ).stream().map(Payment::getValue).reduce(BigDecimal::add
                                    ).orElseGet(() -> BigDecimal.ZERO);
                            return new PaymentsShortStatsDto(it.getName(), sum);
                        }
                ).collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    @Transactional
    public PaymentDto addPayment(PaymentDto paymentDto) {
        Optional<User> user = userRepository.findById(paymentDto.getUserId());
        Optional<Category> category = categoryRepository
                .findByNameAndUserIdAndIncomeLabel(paymentDto.getCategoryName(),
                        paymentDto.getUserId(), paymentDto.getIncomeLabel());
        Subcategory subcategory = null;
        if (paymentDto.getSubcategoryName() != null && !paymentDto.getSubcategoryName().isEmpty()) {
            Optional<Subcategory> subcategoryOptional = subcategoryRepository
                    .findByNameAndCategoryId(paymentDto.getSubcategoryName(), category.get().getId());
            subcategory = subcategoryOptional.get();
        }
        if (user.isPresent()) {
            Payment newPayment = new Payment(BigInteger.valueOf(-1),
                    user.get().getAccount(),
                    user.get(),
                    category.get(),
                    subcategory,
                    paymentDto.getIncomeLabel(),
                    paymentDto.getValue(),
                    Date.valueOf(paymentDto.getDate()));
            updateBalance(user.get().getId(),
                    newPayment.getIncomeLabel() ? newPayment.getValue() : newPayment.getValue().negate());
            paymentRepository.save(newPayment);
            return new PaymentDto(newPayment);
        } else {
            return null;
        }
    }

    private void updateBalance(Integer userId, BigDecimal updateValue) {
        Date timeStart = Date.valueOf(LocalDate.now().withDayOfMonth(1));
        Date timeEnd = Date.valueOf(LocalDate.now());
        Balance balance = balanceRepository.findByUserIdAndMonthBetween(userId, timeStart, timeEnd).get();
        BigDecimal value = balance.getFinalBalance();
        value = value.add(updateValue);
        balance.setFinalBalance(value);
        balanceRepository.save(balance);
    }
}
