package org.knop.budgetKeeper.service;

import lombok.SneakyThrows;
import org.knop.budgetKeeper.dto.*;
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
import java.time.Period;
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

    @Autowired
    private PlanRepository planRepository;

    @Override
    public List<PaymentDto> getAllForUser(Integer userId) {
        return paymentRepository
                .findAllByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Payment::getDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(PaymentDto::new)
                .sorted(Comparator.comparing(PaymentDto::getDate).thenComparing(PaymentDto::getId).reversed())
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
                .sorted(Comparator.comparing(PaymentsShortStatsDto::getCategory))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentsShortStatsDto> getPaymentsStatsByPeriod(AnalyticStatsDto analyticStatsDto) {
        List<Payment> paymentsList = paymentRepository
                .findAllByUserIdAndIncomeLabelAndDateBetween(
                        analyticStatsDto.getUserId(),
                        analyticStatsDto.getIncomeLabel(),
                        analyticStatsDto.getDateStart(),
                        analyticStatsDto.getDateEnd());
        Map<Category, BigDecimal> categoryIncomeSum = new HashMap<>();
        paymentsList.stream()
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
        return categoryIncomeSum
                .entrySet()
                .stream()
                .map(it -> new PaymentsShortStatsDto(
                        it.getKey().getName(),
                        it.getValue()))
                .sorted(Comparator.comparing(PaymentsShortStatsDto::getCategory))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentsShortStatsDto> getStatsByCategory(AnalyticStatsByCategoryDto analyticStatsByCategoryDto) {
        Optional<Category> category = categoryRepository
                .findByNameAndUserIdAndIncomeLabel(
                        analyticStatsByCategoryDto.getCategoryName(),
                        analyticStatsByCategoryDto.getUserId(),
                        analyticStatsByCategoryDto.getIncomeLabel());
        if (category.isEmpty()) {
            return Collections.emptyList();
        }
        List<Subcategory> subcategory = subcategoryRepository
                .findAllByUserIdAndCategoryId(analyticStatsByCategoryDto.getUserId(), category.get().getId());

        if(subcategory.isEmpty()) {
            BigDecimal value =  paymentRepository.findAllByUserIdAndIncomeLabelAndDateBetween(analyticStatsByCategoryDto.getUserId(),
                    analyticStatsByCategoryDto.getIncomeLabel(),
                    analyticStatsByCategoryDto.getDateStart(),
                    analyticStatsByCategoryDto.getDateEnd()).stream()
                    .filter(it -> it.getCategory().getName().equals(analyticStatsByCategoryDto.getCategoryName()))
                    .map(Payment::getValue)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            return List.of(new PaymentsShortStatsDto(
                    analyticStatsByCategoryDto.getCategoryName(),
                    value));
        }
        return subcategory.stream()
                .map(
                        it -> {
                            BigDecimal sum =
                                    paymentRepository.findAllByUserIdAndIncomeLabelAndSubcategoryIdAndDateBetween(
                                            analyticStatsByCategoryDto.getUserId(),
                                            analyticStatsByCategoryDto.getIncomeLabel(),
                                            it.getId(),
                                            analyticStatsByCategoryDto.getDateStart(),
                                            analyticStatsByCategoryDto.getDateEnd()
                                    ).stream().map(Payment::getValue).reduce(BigDecimal::add
                                    ).orElse(BigDecimal.ZERO);
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
                    paymentDto.getValue().abs(),
                    Date.valueOf(paymentDto.getDate()));
            updateBalance(user.get().getId(),
                    newPayment.getIncomeLabel() ? newPayment.getValue() : newPayment.getValue().negate());
            updatePlanProgress(user.get().getId());
            paymentRepository.save(newPayment);
            return new PaymentDto(newPayment);
        } else {
            return null;
        }
    }

    @Override
    public List<PaymentsGraphDto> getGraphStats(Integer userId, Date dateStart, Date dateEnd) {
        Integer daysCount = Period.between(dateStart.toLocalDate(), dateEnd.toLocalDate()).getDays();
        if (daysCount < 66) {
            return getPaymentsGraphStatsByDay(userId, dateStart, dateEnd);
        }
        else {
            return getPaymentsGraphStatsByMonth(userId, dateStart, dateEnd);
        }
    }

    @Override
    public List<UselessPaymentDto> getUselessPayments(Integer userId, Date dateStart, Date dateEnd) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Payment> payments = paymentRepository.findAllByUserIdAndDateBetween(
                    user.get().getId(), dateStart, dateEnd);
            List<UselessPaymentDto> byCategory = payments
                    .stream()
                    .filter(it -> it.getCategory().getUselessType() && it.getSubcategory() == null)
                    .map(it -> new UselessPaymentDto(
                            it.getCategory().getName(),
                            null,
                            it.getValue()))
                    .collect(Collectors.toList());
            Map<UselessPaymentDto, BigDecimal> byCategoryMap = new HashMap<>();
            for(UselessPaymentDto dto: byCategory) {
                BigDecimal value = byCategoryMap.get(dto);
                if(value == null) {
                    value = dto.getValue();
                } else {
                    value = value.add(dto.getValue());
                }
                byCategoryMap.put(dto, value);
            }

            List<UselessPaymentDto> bySubcategory= payments.stream()
                    .filter(it -> it.getSubcategory() != null && it.getSubcategory().getUselessType())
                    .map(it -> new UselessPaymentDto(
                            it.getCategory().getName(),
                            it.getSubcategory().getName(),
                            it.getValue()
                    ))
                    .collect(Collectors.toList());
            Map<UselessPaymentDto, BigDecimal> bySubcategoryMap = new HashMap<>();
            for(UselessPaymentDto dto : bySubcategory) {
                BigDecimal value = bySubcategoryMap.get(dto);
                if(value == null) {
                    value = dto.getValue();
                } else {
                    value = value.add(dto.getValue());
                }
                bySubcategoryMap.put(dto, value);
            }
            List<UselessPaymentDto> result = bySubcategoryMap.entrySet().stream().map(
                    entry -> {
                        UselessPaymentDto dto = entry.getKey();
                        dto.setValue(entry.getValue());
                        return dto;
                    }
            ).collect(Collectors.toList());
            result.addAll(
                    byCategoryMap.entrySet().stream().map(entry -> {
                        UselessPaymentDto dto = entry.getKey();
                        dto.setValue(entry.getValue());
                        return dto;
                    })

                            .collect(Collectors.toList())
            );
            result.sort(Comparator.comparing(UselessPaymentDto::getCategory));
            return result;
        }
        else {
            return Collections.emptyList();
        }
    }

    @Override
    public Boolean deletePayment(PaymentDto paymentDto) {
        Optional<Payment> forDelete = paymentRepository
                .findById(paymentDto.getId());
        if(forDelete.isPresent()) {
            Payment payment = forDelete.get();
            paymentRepository.delete(payment);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    private void updateBalance(Integer userId, BigDecimal updateValue) {
        Date timeStart = Date.valueOf(LocalDate.now().withDayOfMonth(1));
        Date timeEnd = Date.valueOf(LocalDate.now());
        Balance balance = balanceRepository.findByUserIdAndMonthBetween(userId, timeStart, timeEnd).get();
        BigDecimal value = balance.getFinalBalance();
        value = value.add(updateValue);
        balance.setFinalBalance(value);
        balanceRepository.save(balance);
    }

    @Transactional
    private void updatePlanProgress(Integer userId) {
        Date timeStart = Date.valueOf(LocalDate.now().withDayOfMonth(1));
        Date timeEnd = Date.valueOf(LocalDate.now());
        Balance balance = balanceRepository.findByUserIdAndMonthBetween(userId, timeStart, timeEnd).get();
        BigDecimal finalBalance = balance.getFinalBalance();
        List<Plan> plans = planRepository.findAllByUserId(userId);
        if (!plans.isEmpty()) {
            for (Plan plan : plans) {
                if (plan.getIsAccumulate()) {
                    plan.setProgress(
                            finalBalance
                                    .divide(BigDecimal.valueOf(plan.getValue()), 4, RoundingMode.HALF_EVEN)
                                    .multiply(BigDecimal.valueOf(100)).doubleValue());
                }
                planRepository.save(plan);
            }
        }
    }

    private List<PaymentsGraphDto> getPaymentsGraphStatsByDay(Integer userId, Date dateStart, Date dateEnd) {
        List<Payment> paymentsList = paymentRepository.findAllByUserIdAndDateBetween(userId, dateStart, dateEnd);
        Map<Date, PaymentsGraphDto> dateSum = new HashMap<>();

        paymentsList.forEach(
                it -> {
                    PaymentsGraphDto dto = dateSum.get(it.getDate());
                    if (dto == null) {
                        dto = new PaymentsGraphDto();
                    }

                    if (it.getIncomeLabel()) {
                        dto.setIncome(dto.getIncome().add(it.getValue()));
                    } else {
                        dto.setExpense(dto.getExpense().add(it.getValue()));
                    }
                    dateSum.put(it.getDate(), dto);
                }
        );

        DateFormatter formatter = new DateFormatter("dd.MM");
        List<PaymentsGraphDto> res =  dateSum
                .entrySet()
                .stream()
                .map(it -> {
                            PaymentsGraphDto result = it.getValue();
                            result.setDate(formatter.print(it.getKey(), Locale.ITALIAN));
                            return result;
                        }
                )
                .sorted(Comparator.comparing(PaymentsGraphDto::getDate))
                .collect(Collectors.toList());

        for (int i = 1; i < res.size(); i++) {
            PaymentsGraphDto dto = res.get(i);
            PaymentsGraphDto previous = res.get(i - 1);

            dto.setIncome(dto.getIncome().add(previous.getIncome()));
            dto.setExpense(dto.getExpense().add(previous.getExpense()));
        }

        res.forEach(PaymentsGraphDto::setNulls);
        return res;
    }

    private List<PaymentsGraphDto> getPaymentsGraphStatsByMonth(Integer userId, Date dateStart, Date dateEnd) {
        Integer monthCount = Period.between(dateStart.toLocalDate(), dateEnd.toLocalDate()).getMonths() + 1;
        LocalDate date = dateStart.toLocalDate();
        List<Payment> paymentsList = paymentRepository.findAllByUserIdAndDateBetween(userId, dateStart,
                Date.valueOf(date.withDayOfMonth(date.lengthOfMonth())));
        Map<LocalDate, PaymentsGraphDto> dateSum = new HashMap<>();

        paymentsList.forEach(
                        it -> {
                            LocalDate month = it.getDate().toLocalDate().withDayOfMonth(1);
                            PaymentsGraphDto dto = dateSum.get(month);
                            if (dto == null) {
                                dto = new PaymentsGraphDto();
                            }

                            if(it.getIncomeLabel()) {
                               dto.setIncome(dto.getIncome().add(it.getValue()));
                            } else {
                                dto.setExpense(dto.getIncome().add(it.getValue()));
                            }
                            dateSum.put(month, dto);
                        }
                );
        DateFormatter formatter = new DateFormatter("MM.yyyy");
        List<PaymentsGraphDto> res = dateSum
                .entrySet()
                .stream()
                .map(it -> {
                            PaymentsGraphDto result = it.getValue();
                            result.setDate(formatter.print(Date.valueOf(it.getKey()), Locale.ITALIAN));
                            return result;
                        }
                )
                .sorted(Comparator.comparing(PaymentsGraphDto::getDate))
                .collect(Collectors.toList());

        for (int i = 1; i < res.size(); i++) {
            PaymentsGraphDto dto = res.get(i);
            PaymentsGraphDto previous = res.get(i - 1);

            dto.setIncome(dto.getIncome().add(previous.getIncome()));
            dto.setExpense(dto.getExpense().add(previous.getExpense()));
        }

        res.forEach(PaymentsGraphDto::setNulls);
        return res;
    }
}
