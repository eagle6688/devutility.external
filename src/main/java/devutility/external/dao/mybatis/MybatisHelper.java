package devutility.external.dao.mybatis;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import devutility.internal.base.SingletonFactory;
import devutility.internal.cache.MemoryCache;

/**
 * @Description: MybatisHelper
 * @author: Aldwin
 */
public class MybatisHelper {
	// region variables

	private String config;

	// endregion

	// region constructor

	public MybatisHelper(String config) {
		this.config = config;
	}

	// endregion

	// region get SqlSessionFactory

	public SqlSessionFactory getSqlSessionFactory() {
		SqlSessionFactory sqlSessionFactory = MemoryCache.get(config, SqlSessionFactory.class);

		if (sqlSessionFactory != null) {
			return sqlSessionFactory;
		}

		Object locker = SingletonFactory.create(config, Object.class);

		synchronized (locker) {
			if (sqlSessionFactory == null) {
				try (InputStream inputStream = Resources.getResourceAsStream(config)) {
					SqlSessionFactory temp = new SqlSessionFactoryBuilder().build(inputStream);
					sqlSessionFactory = temp;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return sqlSessionFactory;
	}

	// endregion

	// region get SqlSession

	public SqlSession getSqlSession() {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		if (sqlSessionFactory == null) {
			return null;
		}

		return sqlSessionFactory.openSession();
	}

	// endregion
}