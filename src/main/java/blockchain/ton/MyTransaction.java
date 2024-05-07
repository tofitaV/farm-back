package blockchain.ton;

import blockchain.ton.Utils.TransactionHelper;
import com.example.happyfarmer.Models.WalletInfo;
import lombok.SneakyThrows;
import org.assertj.core.api.AssertionsForClassTypes;
import org.ton.java.address.Address;
import org.ton.java.mnemonic.Mnemonic;
import org.ton.java.mnemonic.Pair;
import org.ton.java.smartcontract.wallet.WalletContract;
import org.ton.java.smartcontract.wallet.v4.WalletV4ContractR2;
import org.ton.java.tonlib.types.*;
import org.ton.java.utils.Utils;

import java.util.Arrays;
import java.util.List;

import static blockchain.ton.MyWallet.tonlib;
import static org.ton.java.utils.Utils.formatNanoValue;

public class MyTransaction {



    @SneakyThrows
    public void createTransaction(WalletInfo walletInfo) {
        WalletContract wallet = new MyWallet().createWallet(walletInfo);
        List<String> mnenomicList = Arrays.asList(walletInfo.getMnemonic().split("\\s+"));
        Pair key = Mnemonic.toKeyPair(mnenomicList);
        if (wallet instanceof WalletV4ContractR2 walletV4) {
            ExtMessageInfo extMessageInfo = walletV4.sendTonCoins(tonlib, key.getSecretKey(), wallet.getAddress(), Utils.toNano(0.01));
            AssertionsForClassTypes.assertThat(extMessageInfo.getError().getCode()).isZero();
            Utils.sleep(30);
            System.out.println("Success");
        } else {
            ExtMessageInfo extMessageInfo = new TransactionHelper().sendTonCoins(wallet, tonlib, key.getSecretKey(), wallet.getAddress(), Utils.toNano(0.01));
            AssertionsForClassTypes.assertThat(extMessageInfo.getError().getCode()).isZero();
            Utils.sleep(30);
            System.out.println("Success");
        }
    }

    public List<String> getLastTenTransactionsShortInfo(WalletInfo walletInfo) {
        MyWallet myWallet = new MyWallet();
        WalletContract wallet = myWallet.createWallet(walletInfo);
        Address addr = Address.of(wallet.getAddress());
        FullAccountState fullAccountState = myWallet.getFullAccountState(addr);

        LastTransactionId lastTransactionId = fullAccountState.getLast_transaction_id();
        RawTransactions rawTransactions = tonlib.getAllRawTransactions(addr.toString(true), lastTransactionId.getLt(), lastTransactionId.getHash(), 10);
        List<String> transactionList = rawTransactions.getTransactions()
                .stream()
                .map(tx -> {
                    if ((tx.getIn_msg() != null) && (!tx.getIn_msg().getSource().getAccount_address().equals(""))) {
                        return String.format("%s, %s <<<<< %s : %s ", Utils.toUTC(tx.getUtime()), tx.getIn_msg().getSource().getAccount_address(), tx.getIn_msg().getDestination().getAccount_address(), formatNanoValue(tx.getIn_msg().getValue()));
                    }
                    if (tx.getOut_msgs() != null) {
                        for (RawMessage msg : tx.getOut_msgs()) {
                            return String.format("%s, %s >>>>> %s : %s ", Utils.toUTC(tx.getUtime()), msg.getSource().getAccount_address(), msg.getDestination().getAccount_address(), formatNanoValue(msg.getValue()));
                        }
                    }
                    return "Empty";
                }).toList();
        return transactionList;
    }

    public List<RawTransaction> getLastTenTransaction(WalletInfo walletInfo) {
        MyWallet myWallet = new MyWallet();
        WalletContract wallet = myWallet.createWallet(walletInfo);
        Address addr = Address.of(wallet.getAddress());
        FullAccountState fullAccountState = myWallet.getFullAccountState(addr);

        LastTransactionId lastTransactionId = fullAccountState.getLast_transaction_id();
        RawTransactions rawTransactions = tonlib.getAllRawTransactions(addr.toString(true), lastTransactionId.getLt(), lastTransactionId.getHash(), 10);
        return rawTransactions.getTransactions();
    }
}
