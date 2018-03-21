package devutility.external.test.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	private int id;
	private String name;
	private int age;

	@JsonProperty("Countries")
	private String[] countries;

	private Date birthday;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String[] getCountries() {
		return countries;
	}

	public void setCountries(String[] countries) {
		this.countries = countries;
	}

	public static List<User> getList() {
		List<User> list = new ArrayList<>();
		String[] countries = { "us", "uk", "cn" };

		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setId(1000 + i + 1);
			user.setAge(i + 20);
			user.setBirthday(new Date());
			user.setCountries(countries);
			user.setName(String.format("Name_%s", i + 1));
			list.add(user);
		}

		return list;
	}
}