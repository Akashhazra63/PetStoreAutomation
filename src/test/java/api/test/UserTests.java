package api.test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
	Faker faker;
	User userPayload;
	public Logger logger;
	@BeforeClass
	public void setup() {
		faker=new Faker();
		userPayload=new User();
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUserName(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());	
		
		//logs
		logger=LogManager.getLogger(this.getClass());
	}
	
	@Test(priority=1)
	public void testPostUser() {
		logger.info("***************Creating user***********");
		Response response=UserEndPoints.createUser(userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("***************User is created***********");
	}
	
	@Test(priority=2)
	public void getUserByName() {
		logger.info("***************Reading user info***********");
		Response response=UserEndPoints.readUser(this.userPayload.getUserName());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("***************User info is displayed***********");
	}
	
	@Test(priority=3)
	public void testUpdateUserByName() {
		//update data using payload
		logger.info("***************Updating user***********");
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response=UserEndPoints.updateUser(this.userPayload.getUserName(), userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		//Checking data after update;
		Response responseAfterUpdate=UserEndPoints.readUser(this.userPayload.getUserName());
		
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("***************User is updated***********");
	}
	
	@Test(priority=4)
	public void testDeleteUserByName() {
		logger.info("***************Deleting user***********");
		Response response=UserEndPoints.deleteUser(this.userPayload.getUserName());
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("**************User is deleted***********");
	}
}
