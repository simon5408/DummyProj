/**
 * 
 */
package com.simonsw.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

import com.simonsw.base.entity.Role;
import com.simonsw.base.entity.RoleResource;
import com.simonsw.base.entity.Users;
import com.simonsw.base.entity.UserRole;
import com.simonsw.base.service.RoleResourceService;
import com.simonsw.base.service.UserRoleService;
import com.simonsw.base.service.UserService;

/**
 * @author Simon Lv
 * @since Oct 31, 2013
 */
public class MySecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {
	@Autowired
	protected UserService userService;
	@Autowired
	protected UserRoleService userRoleService;
	@Autowired
	protected RoleResourceService roleResourceService;

	/*
	 * private ISystemService systemService;
	 * 
	 * public ISystemService getSystemService() { return systemService; }
	 * 
	 * public void setSystemService(ISystemService systemService) {
	 * this.systemService = systemService; }
	 */

	private static LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> resourceMap = null;

	// 由spring调用
	/*
	 * public MySecurityMetadataSource(ISystemService systemService) {
	 * this.systemService = systemService; }
	 */

	// 加载所有资源与权限的关系
	private Map<String, String> getResource() {
		Map<String, String> resourceMap = new HashMap<String, String>();
		List<Users> users = userService.findAll();
		List<UserRole> userRoles;
		Role role;
		List<RoleResource> roleResources;
		for (Users user : users) {
			userRoles = userRoleService.getUserRoleByUserId(user.getUserid());
			for (UserRole userRole : userRoles) {
				role = userRole.getRole();
				roleResources = roleResourceService.getUserRoleByRoleId(role.getRoleid());
				for (RoleResource roleResource : roleResources) {
					
					String url = roleResource.getResource().getValue();
					if (!resourceMap.containsKey(url)) {
						resourceMap.put(url, role.getName());
					} else {
						String roleName = resourceMap.get(url);
						resourceMap.put(url, roleName + "," + role.getName());
					}
				}
			}
		}
		return resourceMap;

	}

	private void loadResourceDefine() {
		resourceMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
		Map<String, String> resource = getResource();
		for (Map.Entry<String, String> entry : resource.entrySet()) {
			Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
			configAttributes.add(new SecurityConfig(entry.getValue()));
			resourceMap.put(new AntPathRequestMatcher(entry.getKey()),
					configAttributes);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.access.SecurityMetadataSource#
	 * getAllConfigAttributes()
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.access.SecurityMetadataSource#getAttributes
	 * (java.lang.Object)
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		HttpServletRequest request = ((FilterInvocation) object).getRequest();
		if (null == resourceMap) {
			System.out.println("请求地址 "
					+ ((FilterInvocation) object).getRequestUrl());
			loadResourceDefine();
			System.out.println("我需要的认证：" + resourceMap.toString());
		}
		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : resourceMap
				.entrySet()) {
			if (entry.getKey().matches(request)) {
				return entry.getValue();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.access.SecurityMetadataSource#supports(java
	 * .lang.Class)
	 */
	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}