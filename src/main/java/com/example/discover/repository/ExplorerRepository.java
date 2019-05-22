package com.example.discover.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.discover.POJO.user.Explorer;
import com.example.discover.POJO.user.User;


public interface ExplorerRepository extends UserBaseRepository<Explorer> {
	@Query("SELECT user FROM User user WHERE user.username=:username AND user.password=:password")
	public User findUserByCredentials(@Param("username") String u, @Param("password") String p);

	@Query(value = "select * from Follower as f inner join User as u on f.user_id=u.userid where f.follower_id =:id", nativeQuery = true)
	public List<Explorer> findFollowing(@Param("id") Integer id);

	@Query(value = "select * from follower as f inner join User as u on f.follower_id = u.userid where f.user_id =:id", nativeQuery = true)
	public List<Explorer> findFollowers(@Param("id") Integer id);
}
