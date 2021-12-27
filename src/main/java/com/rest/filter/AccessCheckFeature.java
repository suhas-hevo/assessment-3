package com.rest.filter;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import com.rest.filter.AccessCheck;
import com.rest.filter.AccessAllowFilter;

@Provider
public class AccessCheckFeature implements DynamicFeature {
	
	@Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		
        if (resourceInfo.getResourceMethod().getAnnotation(AccessCheck.class) != null) {
            context.register(AccessAllowFilter.class);
        }
        
    }

}
