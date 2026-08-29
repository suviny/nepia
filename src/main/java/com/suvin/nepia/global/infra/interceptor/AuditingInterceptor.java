package com.suvin.nepia.global.infra.interceptor;

import com.suvin.nepia.global.common.BaseTime;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 데이터베이스에 데이터가 {@code INSERT}되거나 {@code UPDATE}될 때, 생성 일시와 변경 일시를 자동 주입하는 인터셉터
 *
 * @author PARK SU BIN
 * @version 1.0
 */
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AuditingInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 메소드 호출시 전달된 인자들을 가져온다.
        Object[] args = invocation.getArgs();
        // 실행될 매퍼 태그 및 쿼리문 정보를 가져온다.
        MappedStatement statement = (MappedStatement) args[0];
        // 쿼리문에 전달될 실제 파라미터 정보를 가져온다.
        Object parameter = args[1];

        if (parameter instanceof MapperMethod.ParamMap<?> map) {
            // 동일 객체가 다른 키 값으로 중복 주입되는 것을 방지
            Set<Object> audited = new HashSet<>();
            for (Object value : map.values()) {
                if (value != null && audited.add(value)) {
                    dispatchToAudit(value, statement);
                }
            }
        } else {
            dispatchToAudit(parameter, statement);
        }
        return invocation.proceed();
    }

    private void dispatchToAudit(Object parameter, MappedStatement statement) {
        SqlCommandType sqlCommandType = statement.getSqlCommandType();
        /* 쿼리 파라미터가 리스트 또는 컬렉션으로 전달될 경우 대비 */
        if (parameter instanceof Collection<?> collection) {
            for (Object value : collection) {
                setAuditTime(value, sqlCommandType);
            }
        } else {
            setAuditTime(parameter, sqlCommandType);
        }
    }

    private void setAuditTime(Object parameter, SqlCommandType sqlCommandType) {
        if (parameter instanceof BaseTime baseTime) {
            LocalDateTime now = LocalDateTime.now();
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                baseTime.preInsert(now);
            } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                baseTime.preUpdate(now);
            }
        }
    }
}