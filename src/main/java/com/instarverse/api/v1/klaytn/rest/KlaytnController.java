package com.instarverse.api.v1.klaytn.rest;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.Constant;
import com.klaytn.caver.Caver;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class KlaytnController {
	
	// private static final Logger logger = LoggerFactory.getLogger(KlaytnController.class);
	
	/**
	 * NFT 민팅 판매 수 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/klay/getBlockNumber")
	public int getBlockNumber(@RequestParam String uid) throws Exception {
		
		Caver caver = new Caver(Constant.KLAYTN_MAINNET_URL);
		
//		SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_TOKEN);
//		caver.wallet.add(deployer);
//		org.web3j.protocol.core.Request<?,Quantity> 
		String blockHex = caver.rpc.getKlay().getBlockNumber().send().getResult();
		blockHex = caver.utils.stripHexPrefix(blockHex);
		
		return Integer.parseInt(blockHex, 16);
	}
}
