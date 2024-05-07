package blockchain.ton;

import com.example.happyfarmer.Models.WalletInfo;
import lombok.SneakyThrows;
import org.ton.java.address.Address;
import org.ton.java.mnemonic.Mnemonic;
import org.ton.java.mnemonic.Pair;
import org.ton.java.smartcontract.wallet.Options;
import org.ton.java.smartcontract.wallet.Wallet;
import org.ton.java.smartcontract.wallet.WalletContract;
import org.ton.java.tonlib.Tonlib;
import org.ton.java.tonlib.types.*;
import java.util.Arrays;
import java.util.List;

import static org.ton.java.utils.Utils.formatNanoValue;

public class MyWallet {

    public static Tonlib tonlib = Tonlib.builder()
            .ignoreCache(false)
            .testnet(true)
            .build();

    public String getWalletBalance(WalletInfo walletInfo) {
        WalletContract wallet = createWallet(walletInfo);
        Address address = Address.of(wallet.getAddress());
        if (!tonlib.getRawAccountStatus(address).equals("active")) {
            return "Wallet is not deployed";
        }
        FullAccountState fullAccountState = getFullAccountState(address);
        return "TON: " + formatNanoValue(fullAccountState.getBalance());
    }

    public FullAccountState getFullAccountState(Address address) {
        AccountAddressOnly accountAddressOnly = AccountAddressOnly.builder()
                .account_address(address.toString(true))
                .build();

        return tonlib.getAccountState(accountAddressOnly);
    }

    @SneakyThrows
    public WalletContract createWallet(WalletInfo walletInfo) {
        List<String> mnenomicList = Arrays.asList(walletInfo.getMnemonic().split("\\s+"));
        Pair key = Mnemonic.toKeyPair(mnenomicList);
        Wallet wallet = new Wallet(walletInfo.getWalletVersion(), Options.builder().publicKey(key.getPublicKey()).build());
        return wallet.create();
    }


}
