package com.connectly.partnerAdmin.module.utils;

import com.ibm.icu.util.ChineseCalendar;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class DateUtils {

    private static final int LD_SUNDAY = 7;
    private static final int LD_SATURDAY = 6;
    private static final int LD_MONDAY = 1;
    private static final int LUNAR_YEAR_OFFSET = 2637;
    private static final int MONTH_OFFSET = 1;

    public static LocalDateTime makeVBankDueDate(LocalDateTime dateTime) {
        return dateTime.withHour(23).withMinute(59).withSecond(59);
    }

    public static String SolarDays(String yyyy, String date) {
        return Lunar2Solar(yyyy + date).substring(4);
    }

    public static String Lunar2Solar(String yyyymmdd) {
        ChineseCalendar cc = new ChineseCalendar();

        if (yyyymmdd == null) return null;

        String date = yyyymmdd.trim();
        if (date.length() != 8) {
            if (date.length() == 4)
                date = date + "0101";
            else if (date.length() == 6)
                date = date + "01";
            else if (date.length() > 8)
                date = date.substring(0, 8);
            else
                return null;
        }

        cc.set(ChineseCalendar.EXTENDED_YEAR, Integer.parseInt(date.substring(0, 4)) + LUNAR_YEAR_OFFSET);
        cc.set(ChineseCalendar.MONTH, Integer.parseInt(date.substring(4, 6)) - MONTH_OFFSET);
        cc.set(ChineseCalendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)));

        LocalDate solar = Instant.ofEpochMilli(cc.getTimeInMillis()).atZone(ZoneId.of("UTC")).toLocalDate();

        return String.format("%04d%02d%02d", solar.getYear(), solar.getMonth().getValue(), solar.getDayOfMonth());
    }

    public static Set<String> holidayArray(LocalDateTime localDateTime) {
        Set<String> holidaysSet = new HashSet<>();
        String yyyy = String.valueOf(localDateTime.getYear());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 양력 휴일 추가
        holidaysSet.add(yyyy+"0101");   // 신정
        holidaysSet.add(yyyy+"0301");   // 삼일절
        holidaysSet.add(yyyy+"0505");   // 어린이날
        holidaysSet.add(yyyy+"0606");   // 현충일
        holidaysSet.add(yyyy+"0815");   // 광복절
        holidaysSet.add(yyyy+"1003");   // 개천절
        holidaysSet.add(yyyy+"1009");   // 한글날
        holidaysSet.add(yyyy+"1225");   // 성탄절

        // 음력 휴일 추가
        String prev_seol = LocalDate.parse(Lunar2Solar(yyyy+"0101"), formatter).minusDays(1).toString().replace("-","");
        holidaysSet.add(yyyy+prev_seol.substring(4));        // ""
        holidaysSet.add(yyyy+SolarDays(yyyy, "0101"));  // 설날
        holidaysSet.add(yyyy+SolarDays(yyyy, "0102"));  // ""
        holidaysSet.add(yyyy+SolarDays(yyyy, "0408"));  // 석탄일
        holidaysSet.add(yyyy+SolarDays(yyyy, "0814"));  // ""
        holidaysSet.add(yyyy+SolarDays(yyyy, "0815"));  // 추석
        holidaysSet.add(yyyy+SolarDays(yyyy, "0816"));  // ""

        // 대체 공휴일 계산
        addReplacementHolidays(holidaysSet, yyyy, formatter);

        return holidaysSet;
    }

    private static void addReplacementHolidays(Set<String> holidays, String year, DateTimeFormatter formatter) {
        int childDayChk = LocalDate.parse(year+"0505", formatter).getDayOfWeek().getValue();
        if(childDayChk == LD_SUNDAY) {      // 일요일
            holidays.add(year+"0506");
        }
        if(childDayChk == LD_SATURDAY) {  // 토요일
            holidays.add(year+"0507");
        }

        // 설날 대체공휴일 검사
        if(LocalDate.parse(Lunar2Solar(year+"0101"),formatter).getDayOfWeek().getValue() == LD_SUNDAY) {    // 일
            holidays.add(Lunar2Solar(year+"0103"));
        }
        if(LocalDate.parse(Lunar2Solar(year+"0101"),formatter).getDayOfWeek().getValue() == LD_MONDAY) {    // 월
            holidays.add(Lunar2Solar(year+"0103"));
        }
        if(LocalDate.parse(Lunar2Solar(year+"0102"),formatter).getDayOfWeek().getValue() == LD_SUNDAY) {    // 일
            holidays.add(Lunar2Solar(year+"0103"));
        }

        // 추석 대체공휴일 검사
        if(LocalDate.parse(Lunar2Solar(year+"0814"), formatter).getDayOfWeek().getValue() == LD_SUNDAY) {
            holidays.add(Lunar2Solar(year+"0817"));
        }
        if(LocalDate.parse(Lunar2Solar(year+"0815"), formatter).getDayOfWeek().getValue() == LD_SUNDAY) {
            holidays.add(Lunar2Solar(year+"0817"));
        }
        if(LocalDate.parse(Lunar2Solar(year+"0816"), formatter).getDayOfWeek().getValue() == LD_SUNDAY) {
            holidays.add(Lunar2Solar(year+"0817"));
        }
    }

    public static boolean isWeekend(LocalDateTime localDateTime) {
        DayOfWeek day = localDateTime.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    public static LocalDateTime getCancelDay(LocalDateTime updateDateTime) {
        LocalDateTime dueTime = updateDateTime.plusDays(1);

        Set<String> holidays = holidayArray(dueTime);

        while (holidays.contains(dueTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                || isWeekend(dueTime)) {
            dueTime = dueTime.plusDays(1);
        }

        return dueTime;
    }




}
