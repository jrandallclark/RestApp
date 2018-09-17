package com.restapi.entities;

import org.codehaus.jackson.annotate.JsonProperty;

public class Currency {
    private String code, name, minimal_amount;
    private int min_confirmations;
    private boolean is_crypto, is_base_of_enabled_pair, is_quote_of_enabled_pair, has_enabled_pairs;

    public Currency() { }

    public Currency(String code, String name, String minimaAmount, int minConfirmations, boolean isCrypto, boolean isBaseOfEnabledPair, boolean isQuoteOfEnabledPair, boolean hasEnabledPairs) {
        this();
        setCode(code);
        setName(name);
        setMinimalAmount(minimaAmount);
        setMinConfirmations(minConfirmations);
        setIs_Crypto(isCrypto);
        setIs_Base_Of_Enabled_Pair(isBaseOfEnabledPair);
        setIs_Quote_Of_Enabled_Pair(isQuoteOfEnabledPair);
        setHas_Enabled_Pairs(hasEnabledPairs);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinimalAmount() {
        return minimal_amount;
    }

    public void setMinimalAmount(String minimal_amount) {
        this.minimal_amount = minimal_amount;
    }

    public int getMinConfirmations() {
        return min_confirmations;
    }

    public void setMinConfirmations(int min_confirmations) {
        this.min_confirmations = min_confirmations;
    }

    public boolean getIs_Crypto() {
        return is_crypto;
    }

    public void setIs_Crypto(boolean isCrypto) {
        this.is_crypto = isCrypto;
    }

    public boolean getIs_Base_Of_Enabled_Pair() {
        return is_base_of_enabled_pair;
    }

    public void setIs_Base_Of_Enabled_Pair(boolean isBaseOfEnabledPair) {
        this.is_base_of_enabled_pair = isBaseOfEnabledPair;
    }

    public boolean getIs_Quote_Of_Enabled_Pair() {
        return is_quote_of_enabled_pair;
    }

    public void setIs_Quote_Of_Enabled_Pair(boolean isQuoteOfEnabledPair) {
        this.is_quote_of_enabled_pair = isQuoteOfEnabledPair;
    }

    public boolean getHas_Enabled_Pairs() {
        return has_enabled_pairs;
    }

    public void setHas_Enabled_Pairs(boolean hasEnabledPairs) {
        this.has_enabled_pairs = hasEnabledPairs;
    }
}
