package com.instarverse.api.v1.scan;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.instarverse.api.v1.common.util.StringUtil;
import com.instarverse.api.v1.vote.vo.VoteHolderVo;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		test1();
	}
	
	public static void test1() throws Exception {
		// 홀더 조회 API URL
		String token_contract_address = "0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2";
		token_contract_address = "0xd9eabf120be90eb14c4081182a464fe7e6a77610";
		
		String apiUrl = "https://api-cypress-v2.scope.klaytn.com/v2/tokens/" + token_contract_address + "/holders?page=";
		
		// 홀더 정보 조회
		// String requestUri = apiUrl
		int page = 0;
		boolean moreDataExist = true;
		int holder_no = 1;
		
		int vote_seq = 1;
		
		
		// https://api-cypress-v2.scope.klaytn.com/v2/tokens/0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2/holders?page=1
		// https://api-cypress-v2.scope.klaytn.com/v2/tokens/0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2/holders?page=1
		
		do {
			page++;
			
			final HttpClient client = HttpClientBuilder.create().build();
			final HttpGet httpGet = new HttpGet(apiUrl + page);
			
			System.out.println(apiUrl + page);
			
//			// KAS 파라미터
//			URI uri = new URIBuilder(httpGet.getURI())
//					.addParameter("kind", "nft")
//					.addParameter("size", "1000")
//					.build();
//			httpGet.setURI(uri);
			
			httpGet.addHeader("origin", "https://scope.klaytn.com");
			
//			httpGet.addHeader("Authorization", "Basic S0FTS0RZNkxaVEM4RDI0WTFCUk1KTE0zOk1QYU56eDNvak1zRUpUX2lXRHdxZ0RCMWxaekJoT3I1YnhLbnYwREo=");
//			httpGet.addHeader("x-chain-id", "8217");
			
			
			
			
			
			final HttpResponse response = client.execute(httpGet);
			final int responseCode = response.getStatusLine().getStatusCode();
			
			if (responseCode != 200) {
//				logger.debug("ERROR - responseCode: " + responseCode);
				System.out.println("ERROR - responseCode: " + responseCode);
				moreDataExist = false;
//				return holderList;
			}
			
			// JSON 형태 반환값 처리
			ObjectMapper mapper = new ObjectMapper();
			JsonNode returnNode = mapper.readTree(response.getEntity().getContent());
			// ex) {"success":true,"code":0,"result":[{"id":152371622,"createdAt":"2022-05-24T05:42:47.000Z","updatedAt":"2022-05-26T04:03:12.000Z","address":"0x16f5df3c8dd43166492b867079f48db746107cfe","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"199998300000000000000000000000"},{"id":153396660,"createdAt":"2022-05-26T04:08:18.000Z","updatedAt":"2022-09-17T08:14:52.000Z","address":"0x29a0bf027d6dd1b306b3c51b7ef66a94a29465eb","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"77408188968564487923023977364"},{"id":153396094,"createdAt":"2022-05-26T04:07:10.000Z","updatedAt":"2022-09-17T14:36:55.000Z","address":"0x4f34efa763e898f21ba7ead8ff9b963279c6021a","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"72924418932993305466508568023"},{"id":153393072,"createdAt":"2022-05-26T03:58:58.000Z","updatedAt":"2022-09-17T14:35:11.000Z","address":"0x537561b1d6324ab35b5a864744cbbae6043ac327","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"65848304216004117971009330553"},{"id":186891491,"createdAt":"2022-08-16T02:52:56.000Z","updatedAt":"2022-08-16T02:52:56.000Z","address":"0xeeb3875af34b6e006ad9df79bc576727d361e79c","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"11768785332122431415800854472"},{"id":153411739,"createdAt":"2022-05-26T04:44:06.000Z","updatedAt":"2022-09-17T08:02:03.000Z","address":"0xb6cdc9fbad2aef9b74a0717c631723acd8b54dda","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"8416454381835919193622393025"},{"id":167227808,"createdAt":"2022-06-23T22:56:52.000Z","updatedAt":"2022-09-18T00:05:41.000Z","address":"0xa8ef046bf39b135115d79ea1c045a89ceeac4e19","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"7819770256928898451877035309"},{"id":155125292,"createdAt":"2022-05-29T21:31:38.000Z","updatedAt":"2022-07-30T02:02:24.000Z","address":"0xf49cb44b120941661d9b21b8b3f08ea8bf423e2c","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"6835603564291410306808787944"},{"id":155212245,"createdAt":"2022-05-30T01:35:37.000Z","updatedAt":"2022-09-03T11:35:19.000Z","address":"0x7f1debef99533f73a4a54b08a7fd745d6dd761b3","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"5296791455417095165465042581"},{"id":160482566,"createdAt":"2022-06-10T08:47:11.000Z","updatedAt":"2022-08-25T16:29:33.000Z","address":"0x24b3d80bb21d84c59b3b7855e3ce142ada078b24","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"5000000000000000155803374148"},{"id":153678342,"createdAt":"2022-05-26T14:48:43.000Z","updatedAt":"2022-09-16T06:32:06.000Z","address":"0x8fd683e2f2aa6e96c33e63b1687443bac64b3053","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"4179380140729666483324203799"},{"id":153817543,"createdAt":"2022-05-26T23:53:42.000Z","updatedAt":"2022-06-19T23:26:25.000Z","address":"0xa7eb4088c7e244e80d943f5e67449d746a4ddedd","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"3688217906059339190937680152"},{"id":152423947,"createdAt":"2022-05-24T07:52:28.000Z","updatedAt":"2022-09-18T00:05:41.000Z","address":"0x66f53c1c83a7e9453224aa031b81c408b3d9238b","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"2880416676890058224075564569"},{"id":155780898,"createdAt":"2022-05-31T04:13:25.000Z","updatedAt":"2022-08-16T15:57:26.000Z","address":"0xcba508542e2d88089502996127a5d17441586632","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"2142300168815880100665141343"},{"id":155228385,"createdAt":"2022-05-30T02:06:45.000Z","updatedAt":"2022-08-31T00:32:47.000Z","address":"0x2405f54f9d456c23df04a982a6f12ceba9986443","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"1934880243160183380154075047"},{"id":155186588,"createdAt":"2022-05-30T00:29:48.000Z","updatedAt":"2022-07-11T00:05:37.000Z","address":"0x3c51a73cb2b8adec023f618e1fc3a0fd1eda5160","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"1905263209405499848596091719"},{"id":153810872,"createdAt":"2022-05-26T23:33:39.000Z","updatedAt":"2022-09-07T05:25:36.000Z","address":"0x59be44c31c4506b30e4e7cad379022c15a9fa04f","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"1673236455298791404789152667"},{"id":166996500,"createdAt":"2022-06-23T08:04:15.000Z","updatedAt":"2022-06-23T08:04:15.000Z","address":"0x9671e3935ddadf12c027a9724fb7141073912cc7","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"1539412630049297031026937259"},{"id":158164261,"createdAt":"2022-06-05T08:55:14.000Z","updatedAt":"2022-08-31T04:28:29.000Z","address":"0x63c427b38c7c1d3532b0a521a83858dbeffa50ac","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"1521231934058445099781149703"},{"id":177317644,"createdAt":"2022-07-19T05:54:53.000Z","updatedAt":"2022-07-19T05:55:12.000Z","address":"0x5df2be65028439d3d1543cef2981410302eabf22","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"1455086084895905536873238715"},{"id":153686842,"createdAt":"2022-05-26T15:05:03.000Z","updatedAt":"2022-08-18T13:21:19.000Z","address":"0x8caef65597972bbf8ff15e5aae83cd111904a009","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"1217555949089978124258022403"},{"id":153503571,"createdAt":"2022-05-26T08:16:04.000Z","updatedAt":"2022-07-05T17:02:30.000Z","address":"0xf2785acaeea75af2a00d0c42b4b9cd64bebb8709","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"942924148225885295406818093"},{"id":154460464,"createdAt":"2022-05-28T07:24:01.000Z","updatedAt":"2022-07-29T13:59:33.000Z","address":"0xcfa6022bdaae72ebc887a49482f1f596138b29e6","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"814529779963230656276419207"},{"id":173601853,"createdAt":"2022-07-09T03:18:28.000Z","updatedAt":"2022-09-15T18:56:54.000Z","address":"0x53b543ae7313d420ba7fdca72f86e17b997697ff","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"693700208327029394764461561"},{"id":175016736,"createdAt":"2022-07-12T23:52:34.000Z","updatedAt":"2022-07-12T23:53:11.000Z","address":"0xb27635519bdadac8a928a6bb8ac4263c355a46bf","tokenAddress":"0xe5f59ea8b7c9806dc84e8f0862e0f6176f2f9cf2","amountHeld":"662848748573772016717375927"}],"total":"268","page":1,"limit":25}
			// ex) {"success":true,"code":0,"result":[{"id":10718912,"createdAt":"2022-03-04T12:58:56.000Z","updatedAt":"2022-03-04T12:59:33.000Z","address":"0x685db42d0c90b39397f9968e60d447db46461238","tokenAddress":"0xd9eabf120be90eb14c4081182a464fe7e6a77610","tokenCount":"1600"}],"total":1,"page":1,"limit":25}
			
			int total = Integer.parseInt(String.valueOf(returnNode.get("total")).replaceAll("\"", ""));
			int limit = Integer.parseInt(String.valueOf(returnNode.get("limit")).replaceAll("\"", ""));
			JsonNode holderListNode = returnNode.get("result");
			
			if (limit * page >= total) {
				moreDataExist = false;
			}
			
			// 2. 토큰정보 검색 및 가격 설정
			for (int i = 0; i < holderListNode.size(); i++) {
				JsonNode holderNode = holderListNode.get(i);
				String address = String.valueOf(holderNode.get("address")).replaceAll("\"", "");
				String amount = String.valueOf(holderNode.get("amountHeld")).replaceAll("\"", "");
				if (StringUtil.isEmpty(amount)) { // amountHeld 가 null인 경우 NFT로 판단.
					amount = String.valueOf(holderNode.get("tokenCount")).replaceAll("\"", "");
				}
				
				VoteHolderVo voteHolderVo = new VoteHolderVo();
				voteHolderVo.setVote_seq(vote_seq);
				voteHolderVo.setHolder_no(holder_no);
				voteHolderVo.setAddress(address);
				voteHolderVo.setAmount(amount);
				
				System.out.println(holder_no + " address: " + address + " amount: " + amount);
				
				holder_no++;
			}
		} while (moreDataExist);
	}

}
