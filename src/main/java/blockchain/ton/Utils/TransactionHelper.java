package blockchain.ton.Utils;

import org.ton.java.address.Address;
import org.ton.java.smartcontract.types.ExternalMessage;
import org.ton.java.smartcontract.wallet.WalletContract;
import org.ton.java.tonlib.Tonlib;
import org.ton.java.tonlib.types.ExtMessageInfo;

import java.math.BigInteger;

public class TransactionHelper {

    public ExtMessageInfo sendTonCoins(WalletContract walletContract, Tonlib tonlib, byte[] secretKey, Address destinationAddress, BigInteger amount) {
        long seqno = walletContract.getSeqno(tonlib);
        ExternalMessage msg = walletContract.createTransferMessage(secretKey, destinationAddress, amount, seqno);
        return tonlib.sendRawMessage(msg.message.toBase64());
    }
}
