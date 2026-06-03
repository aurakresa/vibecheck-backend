package com.vibecheck.vibecheck_backend.exception


class UnauthorizedException(message: String) : RuntimeException(message)
class UserNotFoundException(message: String) : RuntimeException(message)