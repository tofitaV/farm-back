package blockchain.ton;

import com.example.happyfarmer.Models.WalletInfo;
import com.iwebpp.crypto.TweetNaclFast;
import lombok.SneakyThrows;
import org.ton.java.address.Address;
import org.ton.java.mnemonic.Mnemonic;
import org.ton.java.mnemonic.Pair;
import org.ton.java.smartcontract.wallet.v3.WalletV3R2;
import org.ton.java.smartcontract.wallet.v4.WalletV4R2;
import org.ton.java.tonlib.Tonlib;
import org.ton.java.tonlib.types.AccountAddressOnly;
import org.ton.java.tonlib.types.FullAccountState;
import org.ton.java.utils.Utils;

import java.util.Arrays;
import java.util.List;

import static org.ton.java.utils.Utils.formatNanoValue;

public class MyWallet {

    public static Tonlib tonlib = Tonlib.builder()
            .ignoreCache(false)
            .testnet(false)
            .build();

    public String getWalletBalance(WalletInfo walletInfo) {
        WalletV4R2 wallet = createWallet(walletInfo);
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
    public WalletV4R2 createWallet(WalletInfo walletInfo) {
        List<String> mnenomicList = Arrays.asList(walletInfo.getMnemonic().split("\\s+"));
        Pair key = Mnemonic.toKeyPair(mnenomicList);
        TweetNaclFast.Signature.KeyPair keyPair = Utils.generateSignatureKeyPairFromSeed(key.getSecretKey());

        WalletV4R2 contract1 = WalletV4R2.builder()
                .tonlib(tonlib)
                .keyPair(keyPair)
                .build();
        System.out.println("pub-key: " + Utils.bytesToHex(contract1.getKeyPair().getPublicKey()));
        System.out.println("prv-key: " + Utils.bytesToHex(contract1.getKeyPair().getSecretKey()));
        return contract1;
    }


}
