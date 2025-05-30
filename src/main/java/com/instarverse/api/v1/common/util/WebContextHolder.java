package com.instarverse.api.v1.common.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.ui.context.Theme;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


/**
 * context util
 * 
 * @author ydahn
 *
 */
public final class WebContextHolder {
	
	private static final Logger logger = LoggerFactory.getLogger(WebContextHolder.class);
	
	private static WebContextHolder INSTANCE = new WebContextHolder();
	
	public WebContextHolder get() {
		return INSTANCE;
	}
	
	private WebContextHolder() {
		super();
	}
	
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attributes.getRequest();
	}
	
	public static HttpSession getSession() {
		return getSession(true);
	}
	
	public static HttpSession getSession(boolean create) {
		return getRequest().getSession(create);
	}
	
	public static String getSessionId() {
		return getSession().getId();
	}
	
	public static ServletContext getServletContext() {
		return getSession().getServletContext();	// servlet2.3
	}
	
	public static Locale getLocale() {
		return RequestContextUtils.getLocale(getRequest());
	}
	
	public static Theme getTheme() {
		return RequestContextUtils.getTheme(getRequest());
	}
	
	public static ApplicationContext getApplicationContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	}
	
	public static ApplicationEventPublisher getApplicationEventPublisher() {
		return (ApplicationEventPublisher) getApplicationContext();
	}
	
	public static LocaleResolver getLocaleResolver() {
		return RequestContextUtils.getLocaleResolver(getRequest());
	}
	
	public static ThemeResolver getThemeResolver() {
		return RequestContextUtils.getThemeResolver(getRequest());
	}
	
	public static ResourceLoader getResourceLoader() {
		return (ResourceLoader) getApplicationContext();
	}
	
	public static ResourcePatternResolver getResourcePatternResolver() {
		return (ResourcePatternResolver) getApplicationContext();
	}
	
	public static MessageSource getMessageSource() {
		return (MessageSource) getApplicationContext();
	}
	
	public static ConversionService getConversionService() {
		return getBeanFromApplicationContext(ConversionService.class);
	}
	
	public static DataSource getDataSource() {
		return getBeanFromApplicationContext(DataSource.class);
	}
	
	public static Collection<String> getActiveProfiles() {
		return Arrays.asList(getApplicationContext().getEnvironment().getActiveProfiles());
	}
	
	public static ClassLoader getBeanClassLoader() {
		return ClassUtils.getDefaultClassLoader();
	}
	
	private static <T> T getBeanFromApplicationContext(Class<T> requiredType) {
		try {
			return getApplicationContext().getBean(requiredType);
		} catch (NoUniqueBeanDefinitionException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (NoSuchBeanDefinitionException e) {
			logger.warn(e.getMessage());
			return null;
		}
	}
}
