package com.instarverse.api.v1.twitter.rest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.instarverse.api.v1.common.util.StringUtil;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.twitter.vo.TwitterVo;
import com.instarverse.api.v1.user.vo.UserVo;

import jakarta.servlet.http.HttpServletRequest;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class TwitterController {
	
	private static final Logger logger = LoggerFactory.getLogger(TwitterController.class);
	
	private static final String REQUEST_BASE_URL = "https://api.twitter.com/2";
	private static final String BEARER_TOKEN = "Bearer AAAAAAAAAAAAAAAAAAAAAF5peQEAAAAAtkMVPCKed%2FF2yBL4qElWAewW1vs%3Dhdw8fVQP3kzb4iD5vSIXz8lyLHRrKKW5xMsYgsxG1bimi3k7Ic";
	
	/**
	 * 트위터 사용자 ID 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/twitter/getUserIdByUserName")
	public CommonVo getUserIdByUserName(@RequestParam String user_name) throws Exception {
		TwitterVo twitterVo = new TwitterVo();
		twitterVo.setResultCd("SUCCESS");
		try {
			
			final HttpClient client = HttpClientBuilder.create().build();
			final HttpGet httpGet = new HttpGet(REQUEST_BASE_URL + "/users/by/username/" + user_name);
			
			httpGet.setHeader("Authorization", BEARER_TOKEN);
			
			final HttpResponse response = client.execute(httpGet);
			final int responseCode = response.getStatusLine().getStatusCode();
			
			if (responseCode != 200) {
				twitterVo.setResultCd("FAIL");
				twitterVo.setResultMsg("ResponseCode is not 200.");
				return twitterVo;
			}
			
			// JSON 형태 반환값 처리
			ObjectMapper mapper = new ObjectMapper();
			JsonNode returnNode = mapper.readTree(response.getEntity().getContent());
			
			String userId = String.valueOf(returnNode.get("data").get("id")).replaceAll("\"", "");
			twitterVo.setUser_id(userId);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			twitterVo.setResultCd("FAIL");
			twitterVo.setResultMsg(e.getMessage());
		}
		return twitterVo;
	}
	
	/**
	 * 트위터 팔로잉 체크
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/twitter/checkUserFollowing")
	public CommonVo checkUserFollowing(@RequestParam String user_id, @RequestParam String check_user_name) throws Exception {
		TwitterVo twitterVo = new TwitterVo();
		twitterVo.setResultCd("SUCCESS");
		try {
			twitterVo.setFollowing_yn("N"); // 디폴트 N
			
			String requestBase = REQUEST_BASE_URL + "/users/" + user_id + "/following?max_results=100";
			
			// 페이징을 위한 변수, ""이 되면 루프 종료 
			String nextToken = "";
			
			do {
				/////////////////////////////////////////////////////////////////////////////// 루프
				String paramNextToken = "";
				if (!StringUtil.isEmpty(nextToken)) {
					paramNextToken = "&pagination_token=" + nextToken;
				} else {
					paramNextToken = "";
				}
				final HttpClient client = HttpClientBuilder.create().build();
				final HttpGet httpGet = new HttpGet(requestBase + paramNextToken);
				httpGet.setHeader("Authorization", BEARER_TOKEN);
				
				final HttpResponse response = client.execute(httpGet);
				final int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode != 200) {
					twitterVo.setResultCd("FAIL");
					twitterVo.setResultMsg("ResponseCode is not 200.");
					return twitterVo;
				}
				
				// JSON 형태 반환값 처리
				ObjectMapper mapper = new ObjectMapper();
				JsonNode returnNode = mapper.readTree(response.getEntity().getContent());
				// e.g. tweet_id : 1530220175832002560
				// {"data":[{"id":"1433772079329910784","name":"Jafar Abbas","username":"Abbas_Asghar139"},{"id":"1353378438657187841","name":"sesampath","username":"eranda375"},{"id":"1404326234771931137","name":"Summer","username":"Summer45894751"},{"id":"2767116297","name":"Raouf Deschamps","username":"raouf_deschamps"},{"id":"125332423","name":"dhineshkumar","username":"dhinesh12"},{"id":"1314295319841181703","name":"Fitmark","username":"Fitmark2"},{"id":"1515089990681825284","name":"Gamer girl 40","username":"Gamergirl401"},{"id":"1299227201980645378","name":"Reogdayak","username":"reogdayak"},{"id":"1478668508556709889","name":"Tommy Percelli","username":"blackPlqyboyy"},{"id":"1284359518999068674","name":"Ho Thi Thuy Trang","username":"thuytrang9099"},{"id":"1364174047936929793","name":"FalyDevCrypto","username":"DevFaly"},{"id":"1457974319552741376","name":"Supriow Roy","username":"RoySupriow"},{"id":"1091636088047718401","name":"Musta","username":"Musta530"},{"id":"1418268221732360193","name":"Lokesh Saini","username":"LokeshSainiii"},{"id":"1055411445393846272","name":"indianstar16","username":"indianstar16"},{"id":"1482595620623949827","name":"Babatunde Kayode","username":"Babatun70889062"},{"id":"1468086697824903171","name":"Mr nft","username":"Zakariy59294129"},{"id":"1412455615398432772","name":"Naim hossen","username":"Naimhossen1"},{"id":"1409161191725813769","name":"Shohan Hossain","username":"ShohanH41704563"},{"id":"3248781114","name":"Atok Khambali","username":"AtokKhambali"},{"id":"591520387","name":"bayo bude","username":"budeking1"},{"id":"1099022600641089536","name":"Misho","username":"Yasser87689028"},{"id":"1277623879758823425","name":"Shuvo ahmed","username":"Shuvoah69796857"},{"id":"958188396677013504","name":"nguyễn Đức Chính","username":"BiBong88"},{"id":"1499038290443440131","name":"Jay Arr David","username":"David_Jay_Arr"},{"id":"1411514384975908866","name":"DEGNY ETTE LOUIS","username":"EtteDegny"},{"id":"1365611897077260295","name":"Monir marzouk","username":"marzouk_monir"},{"id":"889493124590030848","name":"Santosh kumar sinha","username":"Santoshkhagaul"},{"id":"366741078","name":"Fatlum (Lumi) Ibishi","username":"lumi_ibishi"},{"id":"2852304078","name":"Dip chowdhury","username":"dip378"},{"id":"3432313765","name":"prakash","username":"prakash70742401"},{"id":"2292526630","name":"Abdullahizaharaddeen","username":"Abdullahizahar4"},{"id":"2917497084","name":"manovijay","username":"manovijay29"},{"id":"1383080597350862852","name":"Mohammad Ariep","username":"ariep_mohammad"},{"id":"1395081163371257858","name":"Deepika juyal","username":"juyal_deepika"},{"id":"1382201912557244417","name":"Harish Kumar","username":"HarishK21290910"},{"id":"1427088877873991682","name":"Airdropworld","username":"Airdropworldie"},{"id":"1433108965664112643","name":"Clara Michelle","username":"claramichou2"},{"id":"1121059424431304705","name":"zNaR","username":"RanzSoriano"},{"id":"1467932923038814212","name":"itsmejeff","username":"itsmeagainjeff"},{"id":"1454405159220441175","name":"Dinesh Kumar","username":"DineshK90229225"},{"id":"1449803112227631106","name":"Poloprudhvi 94","username":"Poloprudhvi94"},{"id":"1469683385756909569","name":"ߦZETLINKSߦ","username":"Maung_Runta"},{"id":"2205323494","name":"KING_OF_NEPAL","username":"koirala_bhaskar"},{"id":"1287667807807614976","name":"Shahin","username":"Shahin63853342"},{"id":"1387785044203229194","name":"samrat420","username":"samrat4202"},{"id":"1533617422489591808","name":"Abdulalem","username":"Abdulal00319286"},{"id":"1430993959728291841","name":"The Test","username":"Mustaqim4208"},{"id":"239860188","name":"Wagner F.dos Santos","username":"Wagnergelo"},{"id":"1468956970195423239","name":"Milly2021","username":"Milly202141"},{"id":"1379290763108884483","name":"Azlie.Hashira","username":"AzlieHashira"},{"id":"1399360691409141768","name":"PAFADNAM ABDOUL KARIM","username":"PAFADNAMABDOUL9"},{"id":"1032797587013857281","name":"Éto","username":"Etoxxxx"},{"id":"1489181468450603012","name":"AWOUDJI Eugène","username":"EugeneAwoudji"},{"id":"153298466","name":"dinero cash","username":"ganasininvertir"},{"id":"582777433","name":"salimovitch45","username":"CMotcho"},{"id":"1454090598495911940","name":"Dedy Indra","username":"DedyInd76"},{"id":"1475396256788889603","name":"Jorge","username":"Jorge142857"},{"id":"1533578623013163010","name":"Taharina Akter","username":"AkterTaharina"},{"id":"1453487146623455239","name":"Best_Aquarius","username":"Best_Aquarius01"},{"id":"1516944665534246912","name":"Farrouj mohamed","username":"Farroujxfar"},{"id":"40109114","name":"icaro tomaz","username":"icarotomaz"},{"id":"1399338238519693315","name":"farbod","username":"farbod90520547"},{"id":"134903031","name":"Laila","username":"lailalima158"},{"id":"1501100595972689921","name":"Sachin Pathare","username":"SachinP81196700"},{"id":"1387422839477731334","name":"Afrith17","username":"Afrith171"},{"id":"1214750678066831360","name":"Maxilus Jhon","username":"MaxilusJ"},{"id":"2979536879","name":"♤MEDO♧","username":"Mumdo7_Essam"},{"id":"1435388006605668354","name":"Djènim.I","username":"I_Djenim"},{"id":"1394739083897839616","name":"Shaikh Rafi","username":"ShaikhR41226703"},{"id":"1296396150832918529","name":"Shakyadeep Dey","username":"DeyShakyadeep"},{"id":"1479719818546667523","name":"Monir","username":"Monir07542020"},{"id":"1039764362603491328","name":"Engr Dauda balami bulus","username":"Dau500"},{"id":"1417518060068491269","name":"EVA","username":"Eva28742105"},{"id":"1382374493532741632","name":"Devendra kumar Sain","username":"Devendr47686792"},{"id":"1496036090519891969","name":"Dhaval Dhamecha","username":"DhavalDhamecha4"},{"id":"1295229467741364226","name":"تركي العجمي","username":"TURKI37520146"},{"id":"1380654108592508930","name":"Francisco","username":"Francisbai21"},{"id":"1054556831761059840","name":"BINIL THOMAS SCARIA","username":"BinilScaria"},{"id":"1490105074298859526","name":"Muhammad Sadis Yazid","username":"yazid_sadis"},{"id":"1457964259267293184","name":"Edeh Ifeanyi","username":"ifeanyiedeh86"},{"id":"1159178730922811393","name":"Ibtissame","username":"ibtissameou"},{"id":"1461425022837346321","name":"osuolale sikiru Babatunde","username":"Skay6599"},{"id":"3277391226","name":"That guy who feels","username":"FeelMachine"},{"id":"1384372402742718467","name":"Md.Monirul Islam","username":"MOni19A9"},{"id":"1530933509614804992","name":"Jsj","username":"11mdmunna114"},{"id":"1375494293075398665","name":"Musa Aycan","username":"MusaAycan5"},{"id":"1305478554281562113","name":"uğur","username":"ugreken21"},{"id":"1530530315113857024","name":"lijo.lzr@gmail.com","username":"lijo_lzr"},{"id":"1488420916145143815","name":"Mohammedhaja","username":"Mohammedhaja9"},{"id":"1376071947180601348","name":"Babo","username":"Babo42604700"},{"id":"1395254355901648898","name":"Shanmugam L","username":"ShanmugamL13"},{"id":"1162754082823532545","name":"Afridi Hassan","username":"AfridiH09441079"},{"id":"1485579838522114059","name":"elitecrypto","username":"elitecrypto1891"},{"id":"1348861262017552385","name":"Anna Kobierzynska","username":"AKobierzynska"}],"meta":{"result_count":95,"next_token":"7140dibdnow9c7btw481d0xkbyu46as2ckgjc6ckmd2k0"}}
				
				JsonNode metaNode = returnNode.get("meta");
				
				// 페이지네이션을 위해 next_token 설정
				nextToken = String.valueOf(metaNode.get("next_token"));
				if (!StringUtil.isEmpty(nextToken)) {
					nextToken = nextToken.replaceAll("\"", "");
				}
				
				JsonNode returnDataNode = returnNode.get("data");
				
				if (returnDataNode == null) {
					break;
				}
				
				// check_user_name 를 팔로잉 중인지 체크 
				for (int i = 0; i < returnDataNode.size(); i++) {
					JsonNode rowObj = returnDataNode.get(i);
					String rowUsername = String.valueOf(rowObj.get("username"));
					if (!StringUtil.isEmpty(rowUsername)) {
						rowUsername = rowUsername.replaceAll("\"", "");
						if (check_user_name.equals(rowUsername)) {
							twitterVo.setFollowing_yn("Y"); // 팔로잉 중일 경우 Y 설정
							return twitterVo;
						}
					}
				}
				
				/////////////////////////////////////////////////////////////////////////////// 루프
			} while (!StringUtil.isEmpty(nextToken)); // nextToken이 없어질 때 까지 루프
			
		} catch (Exception e) {
			e.printStackTrace();
			twitterVo.setResultCd("FAIL");
			twitterVo.setResultMsg(e.getMessage());
		}
		return twitterVo;
	}
	
	/**
	 * 트위터 리트윗 체크
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/twitter/checkTweetsRetweetedBy")
	public CommonVo checkTweetsRetweetedBy(@RequestParam String tweet_id, @RequestParam String check_user_name) throws Exception {
		TwitterVo twitterVo = new TwitterVo();
		twitterVo.setResultCd("SUCCESS");
		try {
			twitterVo.setRetweeted_yn("N"); // 디폴트 N
			
			String requestBase = REQUEST_BASE_URL + "/tweets/" + tweet_id + "/retweeted_by?max_results=100";
			
			// 페이징을 위한 변수, ""이 되면 루프 종료 
			String nextToken = "";
			
			do {
				/////////////////////////////////////////////////////////////////////////////// 루프
				String paramNextToken = "";
				if (!StringUtil.isEmpty(nextToken)) {
					paramNextToken = "&pagination_token=" + nextToken;
				} else {
					paramNextToken = "";
				}
				final HttpClient client = HttpClientBuilder.create().build();
				final HttpGet httpGet = new HttpGet(requestBase + paramNextToken);
				httpGet.setHeader("Authorization", BEARER_TOKEN);
				
				final HttpResponse response = client.execute(httpGet);
				final int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode != 200) {
					twitterVo.setResultCd("FAIL");
					twitterVo.setResultMsg("ResponseCode is not 200.");
					return twitterVo;
				}
				
				// JSON 형태 반환값 처리
				ObjectMapper mapper = new ObjectMapper();
				JsonNode returnNode = mapper.readTree(response.getEntity().getContent());
				// e.g. tweet_id : 1530220175832002560
				// {"data":[{"id":"1433772079329910784","name":"Jafar Abbas","username":"Abbas_Asghar139"},{"id":"1353378438657187841","name":"sesampath","username":"eranda375"},{"id":"1404326234771931137","name":"Summer","username":"Summer45894751"},{"id":"2767116297","name":"Raouf Deschamps","username":"raouf_deschamps"},{"id":"125332423","name":"dhineshkumar","username":"dhinesh12"},{"id":"1314295319841181703","name":"Fitmark","username":"Fitmark2"},{"id":"1515089990681825284","name":"Gamer girl 40","username":"Gamergirl401"},{"id":"1299227201980645378","name":"Reogdayak","username":"reogdayak"},{"id":"1478668508556709889","name":"Tommy Percelli","username":"blackPlqyboyy"},{"id":"1284359518999068674","name":"Ho Thi Thuy Trang","username":"thuytrang9099"},{"id":"1364174047936929793","name":"FalyDevCrypto","username":"DevFaly"},{"id":"1457974319552741376","name":"Supriow Roy","username":"RoySupriow"},{"id":"1091636088047718401","name":"Musta","username":"Musta530"},{"id":"1418268221732360193","name":"Lokesh Saini","username":"LokeshSainiii"},{"id":"1055411445393846272","name":"indianstar16","username":"indianstar16"},{"id":"1482595620623949827","name":"Babatunde Kayode","username":"Babatun70889062"},{"id":"1468086697824903171","name":"Mr nft","username":"Zakariy59294129"},{"id":"1412455615398432772","name":"Naim hossen","username":"Naimhossen1"},{"id":"1409161191725813769","name":"Shohan Hossain","username":"ShohanH41704563"},{"id":"3248781114","name":"Atok Khambali","username":"AtokKhambali"},{"id":"591520387","name":"bayo bude","username":"budeking1"},{"id":"1099022600641089536","name":"Misho","username":"Yasser87689028"},{"id":"1277623879758823425","name":"Shuvo ahmed","username":"Shuvoah69796857"},{"id":"958188396677013504","name":"nguyễn Đức Chính","username":"BiBong88"},{"id":"1499038290443440131","name":"Jay Arr David","username":"David_Jay_Arr"},{"id":"1411514384975908866","name":"DEGNY ETTE LOUIS","username":"EtteDegny"},{"id":"1365611897077260295","name":"Monir marzouk","username":"marzouk_monir"},{"id":"889493124590030848","name":"Santosh kumar sinha","username":"Santoshkhagaul"},{"id":"366741078","name":"Fatlum (Lumi) Ibishi","username":"lumi_ibishi"},{"id":"2852304078","name":"Dip chowdhury","username":"dip378"},{"id":"3432313765","name":"prakash","username":"prakash70742401"},{"id":"2292526630","name":"Abdullahizaharaddeen","username":"Abdullahizahar4"},{"id":"2917497084","name":"manovijay","username":"manovijay29"},{"id":"1383080597350862852","name":"Mohammad Ariep","username":"ariep_mohammad"},{"id":"1395081163371257858","name":"Deepika juyal","username":"juyal_deepika"},{"id":"1382201912557244417","name":"Harish Kumar","username":"HarishK21290910"},{"id":"1427088877873991682","name":"Airdropworld","username":"Airdropworldie"},{"id":"1433108965664112643","name":"Clara Michelle","username":"claramichou2"},{"id":"1121059424431304705","name":"zNaR","username":"RanzSoriano"},{"id":"1467932923038814212","name":"itsmejeff","username":"itsmeagainjeff"},{"id":"1454405159220441175","name":"Dinesh Kumar","username":"DineshK90229225"},{"id":"1449803112227631106","name":"Poloprudhvi 94","username":"Poloprudhvi94"},{"id":"1469683385756909569","name":"ߦZETLINKSߦ","username":"Maung_Runta"},{"id":"2205323494","name":"KING_OF_NEPAL","username":"koirala_bhaskar"},{"id":"1287667807807614976","name":"Shahin","username":"Shahin63853342"},{"id":"1387785044203229194","name":"samrat420","username":"samrat4202"},{"id":"1533617422489591808","name":"Abdulalem","username":"Abdulal00319286"},{"id":"1430993959728291841","name":"The Test","username":"Mustaqim4208"},{"id":"239860188","name":"Wagner F.dos Santos","username":"Wagnergelo"},{"id":"1468956970195423239","name":"Milly2021","username":"Milly202141"},{"id":"1379290763108884483","name":"Azlie.Hashira","username":"AzlieHashira"},{"id":"1399360691409141768","name":"PAFADNAM ABDOUL KARIM","username":"PAFADNAMABDOUL9"},{"id":"1032797587013857281","name":"Éto","username":"Etoxxxx"},{"id":"1489181468450603012","name":"AWOUDJI Eugène","username":"EugeneAwoudji"},{"id":"153298466","name":"dinero cash","username":"ganasininvertir"},{"id":"582777433","name":"salimovitch45","username":"CMotcho"},{"id":"1454090598495911940","name":"Dedy Indra","username":"DedyInd76"},{"id":"1475396256788889603","name":"Jorge","username":"Jorge142857"},{"id":"1533578623013163010","name":"Taharina Akter","username":"AkterTaharina"},{"id":"1453487146623455239","name":"Best_Aquarius","username":"Best_Aquarius01"},{"id":"1516944665534246912","name":"Farrouj mohamed","username":"Farroujxfar"},{"id":"40109114","name":"icaro tomaz","username":"icarotomaz"},{"id":"1399338238519693315","name":"farbod","username":"farbod90520547"},{"id":"134903031","name":"Laila","username":"lailalima158"},{"id":"1501100595972689921","name":"Sachin Pathare","username":"SachinP81196700"},{"id":"1387422839477731334","name":"Afrith17","username":"Afrith171"},{"id":"1214750678066831360","name":"Maxilus Jhon","username":"MaxilusJ"},{"id":"2979536879","name":"♤MEDO♧","username":"Mumdo7_Essam"},{"id":"1435388006605668354","name":"Djènim.I","username":"I_Djenim"},{"id":"1394739083897839616","name":"Shaikh Rafi","username":"ShaikhR41226703"},{"id":"1296396150832918529","name":"Shakyadeep Dey","username":"DeyShakyadeep"},{"id":"1479719818546667523","name":"Monir","username":"Monir07542020"},{"id":"1039764362603491328","name":"Engr Dauda balami bulus","username":"Dau500"},{"id":"1417518060068491269","name":"EVA","username":"Eva28742105"},{"id":"1382374493532741632","name":"Devendra kumar Sain","username":"Devendr47686792"},{"id":"1496036090519891969","name":"Dhaval Dhamecha","username":"DhavalDhamecha4"},{"id":"1295229467741364226","name":"تركي العجمي","username":"TURKI37520146"},{"id":"1380654108592508930","name":"Francisco","username":"Francisbai21"},{"id":"1054556831761059840","name":"BINIL THOMAS SCARIA","username":"BinilScaria"},{"id":"1490105074298859526","name":"Muhammad Sadis Yazid","username":"yazid_sadis"},{"id":"1457964259267293184","name":"Edeh Ifeanyi","username":"ifeanyiedeh86"},{"id":"1159178730922811393","name":"Ibtissame","username":"ibtissameou"},{"id":"1461425022837346321","name":"osuolale sikiru Babatunde","username":"Skay6599"},{"id":"3277391226","name":"That guy who feels","username":"FeelMachine"},{"id":"1384372402742718467","name":"Md.Monirul Islam","username":"MOni19A9"},{"id":"1530933509614804992","name":"Jsj","username":"11mdmunna114"},{"id":"1375494293075398665","name":"Musa Aycan","username":"MusaAycan5"},{"id":"1305478554281562113","name":"uğur","username":"ugreken21"},{"id":"1530530315113857024","name":"lijo.lzr@gmail.com","username":"lijo_lzr"},{"id":"1488420916145143815","name":"Mohammedhaja","username":"Mohammedhaja9"},{"id":"1376071947180601348","name":"Babo","username":"Babo42604700"},{"id":"1395254355901648898","name":"Shanmugam L","username":"ShanmugamL13"},{"id":"1162754082823532545","name":"Afridi Hassan","username":"AfridiH09441079"},{"id":"1485579838522114059","name":"elitecrypto","username":"elitecrypto1891"},{"id":"1348861262017552385","name":"Anna Kobierzynska","username":"AKobierzynska"}],"meta":{"result_count":95,"next_token":"7140dibdnow9c7btw481d0xkbyu46as2ckgjc6ckmd2k0"}}
				
				JsonNode metaNode = returnNode.get("meta");
				
				// 페이지네이션을 위해 next_token 설정
				nextToken = String.valueOf(metaNode.get("next_token"));
				if (!StringUtil.isEmpty(nextToken)) {
					nextToken = nextToken.replaceAll("\"", "");
				}
				
				JsonNode returnDataNode = returnNode.get("data");
				
				if (returnDataNode == null) {
					break;
				}
				
				// check_user_name 를 리트윗 했는지 체크 
				for (int i = 0; i < returnDataNode.size(); i++) {
					JsonNode rowObj = returnDataNode.get(i);
					String rowUsername = String.valueOf(rowObj.get("username"));
					if (!StringUtil.isEmpty(rowUsername)) {
						rowUsername = rowUsername.replaceAll("\"", "");
						if (check_user_name.equals(rowUsername)) {
							twitterVo.setRetweeted_yn("Y"); // 리트윗 완료시 Y 설정
							return twitterVo;
						}
					}
				}
				
				/////////////////////////////////////////////////////////////////////////////// 루프
			} while (!StringUtil.isEmpty(nextToken)); // nextToken이 없어질 때 까지 루프
			
		} catch (Exception e) {
			e.printStackTrace();
			twitterVo.setResultCd("FAIL");
			twitterVo.setResultMsg(e.getMessage());
		}
		return twitterVo;
	}
	
	/**
	 * 트위터 좋아요 체크
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/twitter/checkTweetsLikingUsers")
	public CommonVo checkTweetsLikingUsers(@RequestParam String tweet_id, @RequestParam String check_user_name) throws Exception {
		TwitterVo twitterVo = new TwitterVo();
		twitterVo.setResultCd("SUCCESS");
		try {
			twitterVo.setLiking_yn("N"); // 디폴트 N
			
			String requestBase = REQUEST_BASE_URL + "/tweets/" + tweet_id + "/liking_users?max_results=100";
			
			// 페이징을 위한 변수, ""이 되면 루프 종료 
			String nextToken = "";
			
			do {
				/////////////////////////////////////////////////////////////////////////////// 루프
				String paramNextToken = "";
				if (!StringUtil.isEmpty(nextToken)) {
					paramNextToken = "&pagination_token=" + nextToken;
				} else {
					paramNextToken = "";
				}
				final HttpClient client = HttpClientBuilder.create().build();
				final HttpGet httpGet = new HttpGet(requestBase + paramNextToken);
				httpGet.setHeader("Authorization", BEARER_TOKEN);
				
				final HttpResponse response = client.execute(httpGet);
				final int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode != 200) {
					twitterVo.setResultCd("FAIL");
					twitterVo.setResultMsg("ResponseCode is not 200.");
					return twitterVo;
				}
				
				// JSON 형태 반환값 처리
				ObjectMapper mapper = new ObjectMapper();
				JsonNode returnNode = mapper.readTree(response.getEntity().getContent());
				// e.g. tweet_id : 1530220175832002560
				// {"data":[{"id":"1422431513484963847","name":"Faisal Nehal","username":"faisalnehal4"},{"id":"1542906358692921344","name":"Kucoin Coin Pump","username":"kucoinpump9669"},{"id":"1404088489","name":"ߟ£Vics Purple Puppetߍߍߒ¦","username":"futa_enjoy"},{"id":"1495519652667133953","name":"BIT OPENNEON","username":"BOpenneon"},{"id":"1490869456704266246","name":"BIT OXOIS","username":"BitOxois"},{"id":"1519616884622118912","name":"Lilianna","username":"Liliann94566694"},{"id":"1534065801086517249","name":"Jenny Ryder","username":"JennyosRyder"},{"id":"1504440834032381956","name":"Mudassir Habibu","username":"habibu_mudassir"},{"id":"1531777651849011200","name":"Stella","username":"Stella_Gooch"},{"id":"1517931043042570242","name":"Erich | Non Fungible","username":"Bert_Mint"},{"id":"1506845566877241350","name":"BIT NEXUNE","username":"BNexune"},{"id":"1520806385512062977","name":"Mohammad | Tokenic","username":"TokenicM"},{"id":"1513169923241041920","name":"Viviana","username":"Viviana36748716"},{"id":"1500833759469441025","name":"OXOAS BTC","username":"OxoasB"},{"id":"1523388549780938752","name":"Sargin ߍ ߎ£","username":"Sargin59013585"},{"id":"1433772079329910784","name":"Jafar Abbas","username":"Abbas_Asghar139"},{"id":"788462591412764674","name":"Ranjit Bhagat","username":"RanjitB16060760"},{"id":"1353378438657187841","name":"sesampath","username":"eranda375"},{"id":"1404326234771931137","name":"Summer","username":"Summer45894751"},{"id":"2767116297","name":"Raouf Deschamps","username":"raouf_deschamps"},{"id":"1299227201980645378","name":"Reogdayak","username":"reogdayak"},{"id":"1284359518999068674","name":"Ho Thi Thuy Trang","username":"thuytrang9099"},{"id":"1457974319552741376","name":"Supriow Roy","username":"RoySupriow"},{"id":"1055411445393846272","name":"indianstar16","username":"indianstar16"},{"id":"1468086697824903171","name":"Mr nft","username":"Zakariy59294129"},{"id":"1409161191725813769","name":"Shohan Hossain","username":"ShohanH41704563"},{"id":"3248781114","name":"Atok Khambali","username":"AtokKhambali"},{"id":"591520387","name":"bayo bude","username":"budeking1"},{"id":"1099022600641089536","name":"Misho","username":"Yasser87689028"},{"id":"1277623879758823425","name":"Shuvo ahmed","username":"Shuvoah69796857"},{"id":"958188396677013504","name":"nguyễn Đức Chính","username":"BiBong88"},{"id":"1365611897077260295","name":"Monir marzouk","username":"marzouk_monir"},{"id":"889493124590030848","name":"Santosh kumar sinha","username":"Santoshkhagaul"},{"id":"2852304078","name":"Dip chowdhury","username":"dip378"},{"id":"3432313765","name":"prakash","username":"prakash70742401"},{"id":"2292526630","name":"Abdullahizaharaddeen","username":"Abdullahizahar4"},{"id":"1383080597350862852","name":"Mohammad Ariep","username":"ariep_mohammad"},{"id":"1483578461100953605","name":"OVATOS BTC","username":"OvatosB"},{"id":"1401525857911517184","name":"Hamza","username":"Hamza09783000"},{"id":"1382201912557244417","name":"Harish Kumar","username":"HarishK21290910"},{"id":"1467932923038814212","name":"itsmejeff","username":"itsmeagainjeff"},{"id":"1454405159220441175","name":"Dinesh Kumar","username":"DineshK90229225"},{"id":"1469683385756909569","name":"ߦZETLINKSߦ","username":"Maung_Runta"},{"id":"1287667807807614976","name":"Shahin","username":"Shahin63853342"},{"id":"1387785044203229194","name":"samrat420","username":"samrat4202"},{"id":"1508216895668957184","name":"Jean-Gabriel Paquette","username":"jeangabrieluskd"},{"id":"1533617422489591808","name":"Abdulalem","username":"Abdulal00319286"},{"id":"1430993959728291841","name":"The Test","username":"Mustaqim4208"},{"id":"239860188","name":"Wagner F.dos Santos","username":"Wagnergelo"},{"id":"1379290763108884483","name":"Azlie.Hashira","username":"AzlieHashira"},{"id":"1032797587013857281","name":"Éto","username":"Etoxxxx"},{"id":"1454090598495911940","name":"Dedy Indra","username":"DedyInd76"},{"id":"1453487146623455239","name":"Best_Aquarius","username":"Best_Aquarius01"},{"id":"1516944665534246912","name":"Farrouj mohamed","username":"Farroujxfar"},{"id":"40109114","name":"icaro tomaz","username":"icarotomaz"},{"id":"1399338238519693315","name":"farbod","username":"farbod90520547"},{"id":"134903031","name":"Laila","username":"lailalima158"},{"id":"1214750678066831360","name":"Maxilus Jhon","username":"MaxilusJ"},{"id":"1296396150832918529","name":"Shakyadeep Dey","username":"DeyShakyadeep"},{"id":"1479719818546667523","name":"Monir","username":"Monir07542020"},{"id":"1417518060068491269","name":"EVA","username":"Eva28742105"},{"id":"1509611824257241145","name":"opunk hadik","username":"HadikOpunk"},{"id":"1380654108592508930","name":"Francisco","username":"Francisbai21"},{"id":"1159178730922811393","name":"Ibtissame","username":"ibtissameou"},{"id":"1384372402742718467","name":"Md.Monirul Islam","username":"MOni19A9"},{"id":"1530933509614804992","name":"Jsj","username":"11mdmunna114"},{"id":"1305478554281562113","name":"uğur","username":"ugreken21"},{"id":"1530530315113857024","name":"lijo.lzr@gmail.com","username":"lijo_lzr"},{"id":"1488420916145143815","name":"Mohammedhaja","username":"Mohammedhaja9"},{"id":"1162754082823532545","name":"Afridi Hassan","username":"AfridiH09441079"},{"id":"1473479193128230915","name":"Franciele Gomes Dos Santos","username":"FrangSantos92"},{"id":"1137616764626526209","name":"Kenny mwamba","username":"Kennymwamba3"},{"id":"995271208961650688","name":"daniatia","username":"DaniatiaTia"},{"id":"983646610025574400","name":"Muhammad Sarim","username":"Muhammadsarim53"},{"id":"824315047","name":"kariya kishor","username":"kariya_kishor"},{"id":"1462803813098672130","name":"Mithun Mistry","username":"MithunM39376027"},{"id":"1420799454865616897","name":"Raj sankhla","username":"Rajsank00920221"},{"id":"141508627","name":"Aap Ka Dost Deepak","username":"deepak792"},{"id":"1387368565930663936","name":"DOGEFATHER","username":"DogefatherSafe"},{"id":"799190622","name":"Architect Bhiyi","username":"festusolabiyi"},{"id":"437744023","name":"M adam","username":"m_insideone"},{"id":"1347159213097639939","name":"BitRizs","username":"RizsBit"},{"id":"275788082","name":"Yuriko Asahiro","username":"YurikoAsahiro"},{"id":"883186458844155905","name":"احد آریا","username":"ahadaria92"},{"id":"2478140194","name":"Pixie haikela","username":"claudiahaikela"},{"id":"30066564","name":"kfx","username":"kfixe"},{"id":"1249089240870400002","name":"Vitaly Sosnin","username":"SosninVitaly"},{"id":"1420587291970392069","name":"burung","username":"RudiMuh20285224"},{"id":"1123669820581580800","name":"Piotr","username":"Piotr97632217"},{"id":"1351866627952963585","name":"AL MAMUN","username":"ALMAMUN06247583"},{"id":"1521754611362648064","name":"Sahin123","username":"Sahin1239998007"},{"id":"1221418575828787200","name":"MD ROBIUL ISLAM ߔ¶","username":"Rony94821"},{"id":"1487815944156192773","name":"Eunice","username":"Eunice108927"},{"id":"1475392682805530626","name":"Faruk Ahmed","username":"nk456r"},{"id":"1452493052870754310","name":"Anubrata ghosh","username":"Anubrataghosh15"},{"id":"1452371339629641729","name":"Pulok","username":"Pulok62815454"},{"id":"1372611715435622401","name":"Md.Saruar Hossen","username":"MdSaruarHossen3"}],"meta":{"result_count":97,"next_token":"7140dibdnow9c7btw481d0x9snu61vvscmmzfuxto8do7"}}
				
				JsonNode metaNode = returnNode.get("meta");
				
				// 페이지네이션을 위해 next_token 설정
				nextToken = String.valueOf(metaNode.get("next_token"));
				if (!StringUtil.isEmpty(nextToken)) {
					nextToken = nextToken.replaceAll("\"", "");
				}
				
				JsonNode returnDataNode = returnNode.get("data");
				
				if (returnDataNode == null) {
					break;
				}
				
				// check_user_name 를 좋아요 중인지 체크 
				for (int i = 0; i < returnDataNode.size(); i++) {
					JsonNode rowObj = returnDataNode.get(i);
					String rowUsername = String.valueOf(rowObj.get("username"));
					if (!StringUtil.isEmpty(rowUsername)) {
						rowUsername = rowUsername.replaceAll("\"", "");
						if (check_user_name.equals(rowUsername)) {
							twitterVo.setLiking_yn("Y"); // 좋아요 중일 경우 Y 설정
							return twitterVo;
						}
					}
				}
				
				/////////////////////////////////////////////////////////////////////////////// 루프
			} while (!StringUtil.isEmpty(nextToken)); // nextToken이 없어질 때 까지 루프
			
		} catch (Exception e) {
			e.printStackTrace();
			twitterVo.setResultCd("FAIL");
			// commonVo.setResultMsg("checkUserFollowing Failed..");
			twitterVo.setResultMsg(e.getMessage());
		}
		return twitterVo;
	}
	
	
	
	
	
	
	
	/**
	 * 트위터 로그인 콜백 - 트위터 사이트에서 설정
	 * https://developer.twitter.com/en/portal/projects
	 * 구글 로그인 - 아이디: klaystartup@gmail.com
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/twitter/loginTwitterCallback" , method = {RequestMethod.POST, RequestMethod.GET})
	public CommonVo loginTwitterCallback(@RequestBody UserVo userVo, HttpServletRequest request) throws Exception {
		try {
			logger.debug("■■■■■■■■■■■■■■■■■■■■■ loginTwitterCallback ■■■■■■■■■■■■■■■■■■■■■");
		} catch (Exception e) {
			// 결과코드 : 실패
			userVo.setResultCd("FAIL");
			userVo.setResultMsg(e.toString());
			logger.debug("■■■■■■■■■■■■■■■■■■■■■ e.toString(): " + e.toString());
		}
		return userVo;
	}
}
