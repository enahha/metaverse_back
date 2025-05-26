package com.instarverse.api.v1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value={"com.instarverse.api.v1.*.mapper"})
public class InstarverseBackApplication {
	
	// private static final Logger logger = LoggerFactory.getLogger(InstarverseBackApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(InstarverseBackApplication.class, args);
		
		try {
			// TODO: 반드시 사용 후에 주석처리 할 것!!!!!!
			// String csrfToken = getCsrfToken();
			// String csrfToken = "ExXbxzTv5ZX6topUBJOWS47v3AR51hi1";
			
			// System.out.println(csrfToken); // ExXbxzTv5ZX6topUBJOWS47v3AR51hi1
			
			
			// String sessionId = getSessionId(csrfToken);
			// System.out.println("sessionId: " + sessionId);
			
			
			
			
//			deployContract();
//			createPool();
//			callFunction();
//			checkBalance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 인스타그램 csrf token 취득
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getSessionId(String csrfToken) throws Exception {
		String sessionId = "";
		String url = "https://www.instagram.com/api/v1/web/accounts/login/ajax/";
		String username = "instarverse2023@gmail.com";
		// String password = "star0401!";
		
		// #PWD_INSTAGRAM_BROWSER:10:1697372827:AZ5QAM3f0wLFl5B+CLLCQ102J9E0bbhiLtC8Tt5JWBuEb9Bovp4oiKvbdrXiuCxnT1MzxyTgjiV52gIIhBgCuwz23bD+TZWxYZBc7NK+GHEPbM0d68BjNngZudhr0qaiUlW8qAED5K9JAQZvjA==
		String time = String.valueOf(System.currentTimeMillis() / 1000);
		String encPassword = "#PWD_INSTAGRAM_BROWSER:10:" + time + ":AZ5QAKoXNwlD3PH/pWz2QPQKm7Xsejo1nE7fratsiAjuAz12Ci1vW+KwhRUhKaGk9Xw1shQ+Blt0DqCh4V3O3KIRfhFGIhFTyYoEMY9DO6cIunnXkTMBpP6f0xRrWFDmLgCv8tcqUseys/r55w==";
		
		// String temp = "#PWD_INSTAGRAM_BROWSER:10:" + time + ":";
		
		
		// URL 생성
		URL loginUrl = new URL(url);
		
		// HTTPS 연결 설정
		HttpsURLConnection connection = (HttpsURLConnection) loginUrl.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		
		// 헤더 추가
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36");
		// connection.setRequestProperty("Cookie", "csrftoken=" + csrfToken + "; sessionid=62193881192%3AFhPR1aLEXoXNbw%3A16%3AAYftEzh23aDAfEfGHBDN8rEJphHlVUGRzJxTxe1fLQ; datr=bf0rZZNhNenjRQTi3yhFGHdX; ds_user_id=62193881192; ig_did=372C4777-A93B-43E7-A332-1B0D658F15AE; ig_nrcb=1; mid=ZSv9zgALAAHuJmaSgjNQpBsvHmea; rur=%22NAO%5C05462193881192%5C0541728918517%3A01f7bc86f45350685c5e4e6c573f71d0ab2a026d5ce5582af930e7f1f4214dbce511d596%22;");
		connection.setRequestProperty("Cookie", "csrftoken=" + csrfToken + "; datr=bf0rZZNhNenjRQTi3yhFGHdX; ds_user_id=62193881192; ig_did=372C4777-A93B-43E7-A332-1B0D658F15AE; ig_nrcb=1; mid=ZSv9zgALAAHuJmaSgjNQpBsvHmea; rur=%22NAO%5C05462193881192%5C0541728918517%3A01f7bc86f45350685c5e4e6c573f71d0ab2a026d5ce5582af930e7f1f4214dbce511d596%22;");
		connection.setRequestProperty("X-Csrftoken", csrfToken);
		connection.setRequestProperty("X-Ig-App-Id", "936619743392459");
		connection.setRequestProperty("Origin", "https://www.instagram.com");
		connection.setRequestProperty("Referer", "https://www.instagram.com/accounts/login/");
		
		
		
		
		
		
		// 파라미터를 요청 본문에 포함시킵니다.
//		String requestBody = "username=" + URLEncoder.encode(username, "UTF-8")
//				+ "&enc_password=" + encPassword
//				+ "&optIntoOneTap=" + URLEncoder.encode("false", "UTF-8")
//				+ "&queryParams=" + URLEncoder.encode("{}", "UTF-8")
//				+ "&trustDeviceRecords=" + URLEncoder.encode("{}", "UTF-8");
		
		String requestBody = "username=" + username
				+ "&enc_password=" + encPassword
				+ "&optIntoOneTap=" + "false"
				+ "&queryParams=" + "{}"
				+ "&trustDeviceRecords=" + "{}";
		
		// requestBody = URLEncoder.encode(requestBody);

		try (OutputStream outputStream = connection.getOutputStream()) {
			// byte[] input = requestBody.getBytes("UTF-8");
			// byte[] input = requestBody.getBytes();
			byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
			outputStream.write(input, 0, input.length);
			
			System.out.println(outputStream.toString());
		}
		
		
		
		
		// 연결 수행
		connection.connect();
		
		// 응답 파싱
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
			
			if (inputLine != null && inputLine.indexOf("sessionid") > 0) {
				
				System.out.println(inputLine);
				
				// ex) ...,["XIGSharedData",[],{"raw":"{\"config\":{\"csrf_token\":\"crqs2hhPelEdGBR40iLQ1LH269R7Jrdr\",\"
				
				// 1. 앞부분 문자 제거
				String[] text1 = inputLine.split("sessionid");
				// index 0 : xxxxxxx
				// index 1 : \":\"pSvJJsJEPOJZn3sCdmxf4FndXK7ZIaXa\",\"
				
				// 2. \" 로 스플릿
				String[] text2 = text1[1].split("\\\"");
				// System.out.println(text2[1]); // :\
				// System.out.println(text2[2]); // pSvJJsJEPOJZn3sCdmxf4FndXK7ZIaXa\
				
				sessionId = text2[2].replaceAll("\\\\", "");
				// System.out.println(cstfToken); // pSvJJsJEPOJZn3sCdmxf4FndXK7ZIaXa
				
				break;
			}
		}
		in.close();
		
		return sessionId;
	}
	
	
	
	
	
	
	/**
	 * 인스타그램 csrf token 취득
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getCsrfToken() throws Exception {
		String cstfToken = "";
		String url = "https://www.instagram.com/accounts/login/";
		
		// HTTPS 연결 설정
		URLConnection connection = new URL(url).openConnection();
		if (connection instanceof HttpsURLConnection) {
			((HttpsURLConnection) connection).setRequestMethod("GET");
		}
		
		// 연결 수행
		connection.connect();
		
		// 응답 파싱
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
			
			if (inputLine != null && inputLine.indexOf("csrf_token") > 0) {
				// ex) ...,["XIGSharedData",[],{"raw":"{\"config\":{\"csrf_token\":\"crqs2hhPelEdGBR40iLQ1LH269R7Jrdr\",\"
				
				// 1. 앞부분 문자 제거
				String[] text1 = inputLine.split("csrf_token");
				// index 0 : xxxxxxx
				// index 1 : \":\"pSvJJsJEPOJZn3sCdmxf4FndXK7ZIaXa\",\"
				
				// 2. \" 로 스플릿
				String[] text2 = text1[1].split("\\\"");
				// System.out.println(text2[1]); // :\
				// System.out.println(text2[2]); // pSvJJsJEPOJZn3sCdmxf4FndXK7ZIaXa\
				
				cstfToken = text2[2].replaceAll("\\\\", "");
				// System.out.println(cstfToken); // pSvJJsJEPOJZn3sCdmxf4FndXK7ZIaXa
				
				// break;
			}
		}
		in.close();
		
		return cstfToken;
	}
	
	
	
	
//	static String CONTRACT_ADDRESS_KLAYSTAR = "0x07ffbdba745f3a98ec462385aedcdcd973021671";
//	static String abiJsonStatic = "[{\"constant\":false,\"inputs\":[{\"name\":\"kspAddress\",\"type\":\"address\"},{\"name\":\"tokenA\",\"type\":\"address\"},{\"name\":\"amountA\",\"type\":\"uint256\"},{\"name\":\"tokenB\",\"type\":\"address\"},{\"name\":\"amountB\",\"type\":\"uint256\"},{\"name\":\"fee\",\"type\":\"uint256\"}],\"name\":\"createKctPool\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"kspAddress\",\"type\":\"address\"},{\"name\":\"tokenA\",\"type\":\"address\"},{\"name\":\"amountA\",\"type\":\"uint256\"},{\"name\":\"fee\",\"type\":\"uint256\"}],\"name\":\"createKlayPool\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"from\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"Transfer\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"holder\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"spender\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"Approval\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"tokenA\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountA\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"tokenB\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountB\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"fee\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"exchange\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"exid\",\"type\":\"uint256\"}],\"name\":\"CreatePool\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"tokenA\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountA\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"tokenB\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountB\",\"type\":\"uint256\"}],\"name\":\"ExchangePos\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"tokenA\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountA\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"tokenB\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountB\",\"type\":\"uint256\"}],\"name\":\"ExchangeNeg\",\"type\":\"event\"}]";
//	
//	
//	public static void checkBalance() throws Exception {
//		Caver caver = new Caver(Constant.KLAYTN_MAINNET_URL);
//		// SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_POOL);
//		SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_TOKEN);
//		caver.wallet.add(deployer);
//		
//		// SendOption 설정
//		SendOptions sendOptions = new SendOptions();
//		sendOptions.setFrom(deployer.getAddress());
//		sendOptions.setGas(BigInteger.valueOf(ContractUtil.KLAYSTAR_SEND_OPTIONS_GAS));
//		
//		
//		
//		KIP7 token = caver.kct.kip7.create("0x9986662563f15ad2d4e21ccd388c8ac77068d5be"); // freedom, 100,000,000
//		
//		BigInteger balance = token.balanceOf(deployer.getAddress());
//		
//		logger.info("balance: " + balance);
//		logger.info("balance: " + balance);
//		logger.info("balance: " + balance);
////		Request response =  caver.rpc.klay.getBalance(deployer.getAddress());
////		
////		
////		List decoded = FunctionReturnDecoder.decode(response.getResult(), function.getOutputParameters());
////		
////		logger.info("aaaaa");
//		
//		
//		// String abi = token.getAbi();
//		ContractMethod methodBalance = token.getMethod("balanceOf");
//		
//		
//		Object[] params = {
//				deployer.getAddress()
//		};
//		
//		TransactionReceiptData receipt = methodBalance.send(Arrays.asList(params), sendOptions);
//		
//		List resultList = methodBalance.getOutputs();
//		
////		Map eventMap = token.getEvents();
//		
//		logger.info("Target transactionHash: " + receipt.getTransactionHash());
//		logger.info("Target transactionHash: " + receipt.getTransactionHash());
//		logger.info("Target transactionHash: " + receipt.getTransactionHash());
//		
//	}
	
//	public static void callFunction() throws Exception {
//		Caver caver = new Caver(Constant.KLAYTN_MAINNET_URL);
//		// SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_POOL);
//		SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_TOKEN);
//		caver.wallet.add(deployer);
//		
//		// SendOption 설정
//		SendOptions sendOptions = new SendOptions();
//		sendOptions.setFrom(deployer.getAddress());
//		sendOptions.setGas(BigInteger.valueOf(ContractUtil.KLAYSTAR_SEND_OPTIONS_GAS));
//		
//		
//		com.klaytn.caver.kct.kip7.KIP7 wallet = caver.kct.kip7.create("0xa962946c5b11b32c4de76ecab2f31493719b3801");
//		String abiJson = "[{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"address[]\"},{\"name\":\"\",\"type\":\"uint256[]\"}],\"name\":\"createTokenDistribution\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"address[]\"},{\"name\":\"\",\"type\":\"uint256[]\"}],\"name\":\"refixDistributionRate\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"depositToken\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"address\"}],\"name\":\"distributions\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"address[]\"},{\"name\":\"\",\"type\":\"uint256[]\"}],\"name\":\"createKlayDistribution\",\"outputs\":[],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"}],\"name\":\"validOperator\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"fee\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"depositKlay\",\"outputs\":[],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"refixBlockAmount\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]";
//		Contract contract = caver.contract.create(abiJson, "0xa962946c5b11b32c4de76ecab2f31493719b3801");
//		
//		
//		ContractMethod method = contract.getMethod("fee");
//		
//		
//		// airdrop contract approve KSP
//		Object[] params = {
//		};
//		
//		TransactionReceiptData receipt = method.send(Arrays.asList(params), sendOptions);
//		logger.info("Target transactionHash: " + receipt.getTransactionHash());
//		
//	}
//	
//	public static void createPool() throws Exception {
//		Caver caver = new Caver(Constant.KLAYTN_MAINNET_URL);
//		// SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_POOL);
//		SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_TOKEN);
//		caver.wallet.add(deployer);
//		
//		// com.klaytn.caver.kct.kip7.KIP7 tokenKSP = caver.kct.kip7.create(ContractUtil.CONTRACT_ADDRESS_KSP_FACTORY_KLAYTN);
//		
//		// com.klaytn.caver.kct.kip7.KIP7 tokenKSP = caver.kct.kip7.create("0xc6a2ad8cc6e4a7e08fc37cc5954be07d499e7654");
//		
//		// com.klaytn.caver.kct.kip7.KIP7 token = caver.kct.kip7.create("0xa749a5fc259a95e81d8579ed37ab5fe14aa39c35");
//		
//		
//		
////		BigInteger allowedBalance = tokenKSP.allowance(deployer.getAddress(), tokenKSP.getContractAddress());
////		String owner = "0x25A3E75A29f1e55F950af059060E05AEd78D95b1";
////		String spender = "0xc6a2ad8cc6e4a7e08fc37cc5954be07d499e7654";
////		BigInteger allowedBalance = token.allowance(owner, spender);
////		System.out.println(allowedBalance);
//		
//		
//		
//		com.klaytn.caver.kct.kip7.KIP7 wallet = caver.kct.kip7.create("0xc6a2ad8cc6e4a7e08fc37cc5954be07d499e7654");
//		String owner = "0x25A3E75A29f1e55F950af059060E05AEd78D95b1";
//		String spender = "0xc6a2ad8cc6e4a7e08fc37cc5954be07d499e7654";
//		BigInteger allowedBalance = wallet.allowance(owner, spender);
//		System.out.println(allowedBalance);
//		
////		Map<String, ContractMethod> methodList2 = tokenKSP.getMethods();
////		
////		Contract contract = caver.contract.create(
////				abiJsonStatic
////				, ContractUtil.CONTRACT_ADDRESS_KSP_FACTORY_KLAYTN);
////		
////		Map<String, ContractMethod> methodList = contract.getMethods();
////		
//////		com.klaytn.caver.kct.kip7.KIP7 tokenKSTAR =  caver.kct.kip7.create(CONTRACT_ADDRESS_KLAYSTAR);
////		
////		// Map map = tokenKSP.detectInterface();
////		// BigInteger balance = tokenKSP.balanceOf("0xb4285d543F192859cdB9f825686F3a2A8f8AA8BC");
////		// System.out.println(balance);
////		
//////		BigInteger allowedBalance = tokenKSP.allowance(deployer.getAddress(), tokenKSP.getContractAddress());
//////		System.out.println(allowedBalance);
////		
////		
////		ContractMethod methodCreateKlayPool = contract.getMethod("createKlayPool");
////		
////		System.out.println(methodCreateKlayPool);
//		
//		
//		
//		// caver.kct.kip7.detectInterface(ContractUtil.CONTRACT_ADDRESS_KSP_FACTORY_KLAYTN)
//	}
//	
//	
//	/**
//	 * Deploy New Contract
//	 * @throws Exception
//	 */
//	public static void deployContract() throws Exception {
//		Caver caver = new Caver(Constant.KLAYTN_MAINNET_URL);
//		// SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_POOL);
//		SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_TOKEN);
//		caver.wallet.add(deployer);
//		
//		String abiJson = "[{\"constant\":false,\"inputs\":[{\"name\":\"kspAddress\",\"type\":\"address\"},{\"name\":\"tokenA\",\"type\":\"address\"},{\"name\":\"amountA\",\"type\":\"uint256\"},{\"name\":\"tokenB\",\"type\":\"address\"},{\"name\":\"amountB\",\"type\":\"uint256\"},{\"name\":\"fee\",\"type\":\"uint256\"}],\"name\":\"createKctPool\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"kspAddress\",\"type\":\"address\"},{\"name\":\"tokenA\",\"type\":\"address\"},{\"name\":\"amountA\",\"type\":\"uint256\"},{\"name\":\"fee\",\"type\":\"uint256\"}],\"name\":\"createKlayPool\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":true,\"stateMutability\":\"payable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"from\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"Transfer\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"holder\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"spender\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"Approval\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"tokenA\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountA\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"tokenB\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountB\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"fee\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"exchange\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"exid\",\"type\":\"uint256\"}],\"name\":\"CreatePool\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"tokenA\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountA\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"tokenB\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountB\",\"type\":\"uint256\"}],\"name\":\"ExchangePos\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"tokenA\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountA\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"tokenB\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amountB\",\"type\":\"uint256\"}],\"name\":\"ExchangeNeg\",\"type\":\"event\"}]";
//		String byteCode = "0x608060405234801561001057600080fd5b506104fe806100206000396000f3fe6080604052600436106100295760003560e01c80634c606fc51461002e578063c3df35e5146100e8575b600080fd5b6100ce600480360360c081101561004457600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919080359060200190929190505050610178565b604051808215151515815260200191505060405180910390f35b61015e600480360360808110156100fe57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919080359060200190929190505050610344565b604051808215151515815260200191505060405180910390f35b6000808773ffffffffffffffffffffffffffffffffffffffff168787878787604051602401808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018581526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828152602001955050505050506040516020818303038152906040527fcf645ccf000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b602083106102cb57805182526020820191506020810190506020830392506102a8565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d806000811461032d576040519150601f19603f3d011682016040523d82523d6000602084013e610332565b606091505b50509050809150509695505050505050565b6000808573ffffffffffffffffffffffffffffffffffffffff16858585604051602401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200182815260200193505050506040516020818303038152906040527fae59acab000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff83818316178352505050506040518082805190602001908083835b6020831061045b5780518252602082019150602081019050602083039250610438565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d80600081146104bd576040519150601f19603f3d011682016040523d82523d6000602084013e6104c2565b606091505b505090508091505094935050505056fea165627a7a72305820b449f604ad2a8c77e028708e00790ecca08a35219040f19fbf904d45243b586d0029";
//		
//		// 컨트랙트 GET
//		Contract contract = caver.contract.create(abiJson);
//		
//		// SendOption 설정
//		SendOptions sendOptions = new SendOptions();
//		sendOptions.setFrom(deployer.getAddress());
//		sendOptions.setGas(BigInteger.valueOf(ContractUtil.KLAYSTAR_SEND_OPTIONS_GAS));
//		
//		Object[] paramsDeploy = {};
//		ContractDeployParams deployParam = new ContractDeployParams(byteCode, paramsDeploy);
//		
//		// 토큰 생성 - constructor에서 생성
//		Contract newContract = contract.deploy(deployParam, sendOptions);
//		
//		// 실행 결과 컨트랙트 주소 설정
//		System.out.println("■■■■■■■ new contract address ■■■■■■■");
//		System.out.println(newContract.getContractAddress());
//		System.out.println("■■■■■■■ new contract address ■■■■■■■");
//	}
}
