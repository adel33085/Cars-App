package com.alexander.base.platform

abstract class BaseUseCase<Repository : IBaseRepository>(val repository: Repository)
