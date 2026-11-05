package com.bluemoon.bfms;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/***
 * Lớp cơ sở cho test CẦN Spring context (repository thật trên H2, MockMvc).
 * Mỗi test tự rollback nhờ @Transactional nên dữ liệu không rò rỉ giữa các
 * test.
 ***/

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class BaseTest {

}
