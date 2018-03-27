package devutility.external.test.basic.lang.reflect.field;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.devutility.test.entity.mongo.Student;

import devutility.internal.test.BaseTest;
import devutility.internal.test.TestExecutor;

public class GetAnnotatedTypeTest extends BaseTest {
	@Override
	public void run() {
		Class<Student> clazz = Student.class;
		Field[] declaredFields = clazz.getDeclaredFields();

		for (Field declaredField : declaredFields) {
			println(declaredField.getName());
			Annotation[] annotations = declaredField.getAnnotations();
			println(annotations.length);

			for (Annotation annotation : annotations) {
				println(annotation.annotationType().getName());

				if (org.springframework.data.annotation.Id.class.equals(annotation.annotationType())) {
					println(String.format("Key Field: %s", declaredField.getName()));
				}

				if (org.springframework.data.mongodb.core.mapping.Field.class.equals(annotation.annotationType())) {
					org.springframework.data.mongodb.core.mapping.Field field = org.springframework.data.mongodb.core.mapping.Field.class.cast(annotation);
					println(String.format("Field: %s", field.value()));
				}
			}
		}
	}

	public static void main(String[] args) {
		TestExecutor.run(GetAnnotatedTypeTest.class);
	}
}