package com.suvin.nepia.global.infra.audit;

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
 * SQL 쿼리 작업 전 {@link BaseTime}을 상속받은 객체의 생성 및 변경 일시를 자동 주입하는 마이바티스 인터셉터
 *
 * @author PARK SU BIN
 * @version 1.0
 */
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AuditTimeInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // SQL 쿼리에 대한 메타 데이터(매핑 정보와 파라미터 등) 가져오기
        Object[] args = invocation.getArgs();
        // 실행될 매퍼 XML 태그 및 쿼리문 정보 가져오기
        MappedStatement statement = (MappedStatement) args[0];
        // 쿼리문에 전달될 실제 파라미터 정보 가져오기
        Object parameter = args[1];

        if (parameter instanceof MapperMethod.ParamMap<?> map) {
            Set<Object> audited = new HashSet<>();
            for (Object value : map.values()) {
                /* 동일 객체가 다른 키값으로 중복 주입되는 것을 방지 */
                if (value != null && audited.add(value)) {
                    dispatchAuditing(value, statement);
                }
            }
        } else {
            dispatchAuditing(parameter, statement);
        }
        return invocation.proceed();
    }

    private void dispatchAuditing(Object parameter, MappedStatement statement) {
        SqlCommandType sqlCommandType = statement.getSqlCommandType();
        /* 전달된 쿼리 파라미터가 리스트와 같은 컬렉션 객체인 경우, 그 길이만큼 내부 객체들을 순회 */
        if (parameter instanceof Collection<?> collection) {
            for (Object obj : collection) {
                invokeAuditTime(obj, sqlCommandType);
            }
        }
        invokeAuditTime(parameter, sqlCommandType);
    }

    private void invokeAuditTime(Object parameter, SqlCommandType commandType) {
        if (parameter instanceof BaseTime baseTime) {
            LocalDateTime now = LocalDateTime.now();
            if (SqlCommandType.INSERT.equals(commandType)) {
                baseTime.beforeInsert(now);
            } else if (SqlCommandType.UPDATE.equals(commandType)) {
                baseTime.beforeUpdate(now);
            }
        }
    }
}