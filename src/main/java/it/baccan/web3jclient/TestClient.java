/*
 * Copyright (c) 2018 Matteo Baccan
 * Distributed under the MIT software license, see the accompanying
 * file COPYING or http://www.opensource.org/licenses/mit-license.php.
 */
package it.baccan.web3jclient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

/**
 *
 * @author Matteo Baccan
 */
public class TestClient {

    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(TestClient.class.getName());

    /**
     *
     * @param argv
     */
    static public void main(String[] argv) {
        log.info("INI");
        
        // Test connection with a infuria node
        String network = "https://ropsten.infura.io/v3/02f814448f5c44428b7eb0ee5f1236a9";
        Web3j web3 = Web3j.build(new HttpService(network));  // defaults to http://localhost:8545/
        Web3ClientVersion web3ClientVersion;
        try {
            log.info("Try to connect with a local node");
            web3ClientVersion = web3.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            log.info(clientVersion);
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        Credentials credentials = null;
        try {
            log.info("Load credentias");
            // https://ropsten.etherscan.io/address/0xf2a4b44365b640585160782690f88677e4757bc2
            credentials = WalletUtils.loadCredentials("password", "UTC--2018-04-07T13-29-19.241613200Z--f2a4b44365b640585160782690f88677e4757bc2.json");
        } catch (IOException | CipherException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        try {
            log.info("Transfer funds");
            TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    web3,
                    credentials,
                    "0xef0164faf54177b413bd789f318245eb59297fce",
                    BigDecimal.valueOf(0.01),
                    Convert.Unit.ETHER).send();
            // https://ropsten.etherscan.io/address/0xef0164faf54177b413bd789f318245eb59297fce
            log.info(transactionReceipt.toString());
        } catch (InterruptedException ex) {
            log.log(Level.SEVERE, null, ex);
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (TransactionException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }

        log.info("END");
        System.exit(0);
    }
}
