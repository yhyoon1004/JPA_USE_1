package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	@Bean //hibernate 라이브러리를 이용하여 lazy 로딩으로 인해 프록시 객체의 값을 가져와 에러가 발생하는 문제를 해결해주는 라이브러리. lazy 로딩이면 값을 가져오지 않는다.
	Hibernate5Module hibernate5Module() {
		Hibernate5Module hibernate5Module = new Hibernate5Module(); //기본 설정 = lazy loading 으로 설정된 엔티티의 proxy 객체에서 값이 넣어져있는 것만 api에 값을 반환시킴
//		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);//해당 옵션은 lazy 로딩일 경우 강제로 값을 가져오는 설정
		return hibernate5Module;
	}

}
