package com.vibecheck.vibecheck_backend.filter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class FirebaseTokenFilter : OncePerRequestFilter() {

    // Konstruktor kosong, tidak butuh ObjectMapper lagi
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        // Kalau tidak ada token Bearer, biarkan lewat (nanti diblokir oleh SecurityConfig)
        if (authHeader.isNullOrEmpty() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)

        try {
            // Verifikasi token ke server Firebase
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)

            // Set context security Spring pakai UID Firebase
            val authentication = UsernamePasswordAuthenticationToken(decodedToken.uid, decodedToken, emptyList())
            SecurityContextHolder.getContext().authentication = authentication

            // Lanjut ke endpoint tujuan
            filterChain.doFilter(request, response)

        } catch (e: FirebaseAuthException) {
            // Jika token palsu atau expired, hapus context
            SecurityContextHolder.clearContext()

            // Tolak request langsung dari Filter dengan format JSON manual
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"

            // Gunakan Raw JSON String untuk menghindari dependensi ObjectMapper
            val jsonResponse = """
                {
                    "status": 401,
                    "message": "Akses Ditolak: Token tidak valid atau sudah kedaluwarsa",
                    "data": null
                }
            """.trimIndent()

            response.writer.write(jsonResponse)
            response.writer.flush()
        }
    }
}