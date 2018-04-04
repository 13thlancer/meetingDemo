/**
 * Copyright (c) 2015-2017, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.v4ward.core.aop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.v4ward.core.model.Menu;
import com.v4ward.core.kit.HttpKit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static com.v4ward.core.kit.HttpKit.getSession;

/**
 * AOP 权限自定义检查
 */
@Aspect
@Component
public class PermissionAop {

    @Pointcut(value = "@annotation(com.v4ward.core.annotation.Permission)")
    private void cutPermission() {

    }

    @Around("cutPermission()")
    public Object doPermission(ProceedingJoinPoint point) throws Throwable {
//        MethodSignature ms = (MethodSignature) point.getSignature();
//        Method method = ms.getMethod();
//        Permission permission = method.getAnnotation(Permission.class);
//        Object[] permissions = permission.value();


        HttpSession session = getSession();

        List<Menu> auths = (List<Menu>) session.getAttribute("auths");

        JSONArray Jmenus = (JSONArray) session.getAttribute("menus");
        String js= JSONObject.toJSONString(Jmenus, SerializerFeature.WriteClassName);//将array数组转换成字符串
        List<Map>  menus = JSONObject.parseArray(js, Map.class);//把字符串转换成集合

        String url = HttpKit.getRequest().getRequestURI();

        for(Map map:menus){
            if(url.equals(map.get("url").toString()) || url.contains(map.get("url").toString())){
                return point.proceed();
            }
        }


        for(Menu auth:auths){
            if(url.equals(auth.getUrl()) || url.contains(auth.getUrl())){
                return point.proceed();
            }
        }
        throw new NoPermissionException();

    }

}
