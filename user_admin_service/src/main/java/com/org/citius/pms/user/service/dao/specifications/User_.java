package com.org.citius.pms.user.service.dao.specifications;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.org.citius.pms.user.service.dao.User;
import com.org.citius.pms.user.service.dao.UserRole;
import com.org.citius.pms.user.service.dao.UserStatus;

@StaticMetamodel(User.class)
public class User_ {

	public static volatile SingularAttribute<User, String> firstName;

	public static volatile SingularAttribute<User, String> lastName;

	public static volatile SingularAttribute<User, String> emailId;

	public static volatile SingularAttribute<User, UserStatus> userStatus;

	public static volatile SingularAttribute<User, Long> id;

	public static volatile SingularAttribute<User, UserRole> userRole;

	public static final String FIRST_NAME = "firstName";

	public static final String LAST_NAME = "lastName";

	public static final String USER_STATUS = "userStatus";

	public static final String USER_ROLE = "userRole";

	public static final String ID = "id";

	public static final String EMAIL_ID = "emailId";

}