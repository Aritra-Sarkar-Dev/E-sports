package com.example.core.config

/**
 * Global Application & Compliance Configuration.
 *
 * IMPORTANT BUSINESS RULE (India Law & Policy Compliance):
 * Do NOT build a paid-entry / stake-based system by default.
 * In India, skill-based gaming and fantasy/esports tournament guidelines mandate
 * clear distinction from gambling/wagering. Tournaments are free to enter, with
 * prize pools funded by brand sponsors and esports organizers.
 * Any entry-fee, deposit, or paid wallet code is gated behind `enablePaidEntry = false`.
 */
object AppConfig {
    /**
     * Stays false by default. When false:
     * - All tournament entries are FREE.
     * - Prizes are 100% sponsored.
     * - Deposit options and entry-fee deductions are completely disabled.
     */
    const val enablePaidEntry: Boolean = false

    const val APP_NAME: String = "FF Arena"
    const val CURRENCY_SYMBOL: String = "₹"
    const val DEFAULT_REGION: String = "India (IN)"
    const val DEFAULT_SPONSOR: String = "Red Bull Esports & ROG India"
}
