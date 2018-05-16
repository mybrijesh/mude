package us.mude.util;


import org.javamoney.moneta.Money;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.persistence.AttributeConverter;

public class MoneyConverter implements AttributeConverter<Money, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Money money) {
        return money.getNumber().intValue();
    }

    @Override
    public Money convertToEntityAttribute(Integer m) {
        Money money = null;
        try {
            CurrencyUnit usd = Monetary.getCurrency("USD");
            money = Money.of(m, usd);
        } catch(Exception e) {
        }
        return money;
    }
}
