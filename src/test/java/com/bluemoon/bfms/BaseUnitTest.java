package com.bluemoon.bfms;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Lớp cơ sở cho unit test thuần Mockito (AuthServiceTest,
 * ApartmentServiceTest).
 * Không nạp Spring context.
 */

@ExtendWith(MockitoExtension.class)
public abstract class BaseUnitTest {

}
