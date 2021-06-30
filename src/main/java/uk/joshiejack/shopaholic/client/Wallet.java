package uk.joshiejack.shopaholic.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.shopaholic.api.gold.WalletType;

import java.util.EnumMap;

@OnlyIn(Dist.CLIENT)
public class Wallet {
    private static final EnumMap<WalletType, Wallet> WALLETS = new EnumMap<WalletType, Wallet>(WalletType.class) {{
        put(WalletType.PERSONAL, new Wallet());
        put(WalletType.SHARED, new Wallet());
    }};

    private static Wallet active = WALLETS.get(WalletType.PERSONAL);
    private long balance, income, expenses;

    public static Wallet getWallet(WalletType type) {
        return WALLETS.get(type);
    }

    public static void setActive(WalletType type) {
        active = getWallet(type);
    }

    public static Wallet getActive() {
        return active;
    }

    public static void setGold(WalletType type, long gold, long income, long expenses) {
        Wallet wallet = WALLETS.get(type);
        wallet.balance = gold;
        wallet.income = income;
        wallet.expenses = expenses;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }

    public long getExpenses() {
        return expenses;
    }

    public long getIncome() {
        return income;
    }
}
