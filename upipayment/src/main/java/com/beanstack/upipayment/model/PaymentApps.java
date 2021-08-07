package com.beanstack.upipayment.model;

import lombok.Getter;

@Getter
public enum PaymentApps {
    NONE(Package.NONE),
    AMAZON_PAY(Package.AMAZON_PAY),
    BHIM_UPI(Package.BHIM_UPI),
    GOOGLE_PAY(Package.GOOGLE_PAY),
    PAYTM(Package.PAYTM),
    PHONE_PE(Package.PHONE_PE);

    private final String mPackageName;

    PaymentApps(String mPackageName) {
        this.mPackageName = mPackageName;
    }


    private static final class Package {
        static final String AMAZON_PAY = "in.amazon.mShop.android.shopping";
        static final String BHIM_UPI = "in.org.npci.upiapp";
        static final String GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user";
        static final String PHONE_PE = "com.phonepe.app";
        static final String PAYTM = "net.one97.paytm";
        private static final String NONE = "";
    }
}
