package com.demo.user.repository;

import com.demo.user.repository.dto.UserPreview;
import com.demo.user.repository.dto.UserPreviewImpl;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.demo.user.repository.entity.QUser.user;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserPreview> findPreviewByUsername(String username) {
        UserPreview result = this.queryFactory
                .select(userPreview())
                .from(user)
                .where(user.username.eq(username))
                .fetchFirst();
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsByUsername(String username) {
        Integer result = this.queryFactory.selectOne()
                .from(user)
                .where(user.username.eq(username))
                .fetchOne();
        return result != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        Integer result = this.queryFactory.selectOne()
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();
        return result != null;
    }

    @Override
    public boolean existsByPhone(String phone) {
        Integer result = this.queryFactory.selectOne()
                .from(user)
                .where(user.phone.eq(phone))
                .fetchOne();
        return result != null;
    }

    private ConstructorExpression<UserPreview> userPreview() {
        return Projections.constructor(
                UserPreviewImpl.class,
                user.username,
                user.password,
                user.email,
                user.phone,
                user.firstName,
                user.lastName,
                user.mfaEnabled,
                user.mfaMethod,
                user.createdAt,
                user.updatedAt
        );
    }
}
