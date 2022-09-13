package com.gerwalex.views;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class MyConverter {
    @SuppressLint("ConstantLocale")
    public static final double units = Math.pow(10, Currency
            .getInstance(Locale.getDefault())
            .getDefaultFractionDigits());
    private static final DateFormat di = DateFormat.getDateInstance(DateFormat.DEFAULT);

    public static String convertCurrency(BigDecimal amount) {
        return amount != null ? NumberFormat
                .getCurrencyInstance()
                .format(amount) : null;
    }

    /**
     * Convertiert ein Date
     */
    public static String convertDate(@Nullable Date date) {
        return date == null ? null : di.format(date);
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    public static String convertPercent(double value) {
        return String.format("%.2f%%", value * 100);
    }
}