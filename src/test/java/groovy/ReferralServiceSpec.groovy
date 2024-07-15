package groovy

import com.example.happyfarmer.Services.ReferralService
import spock.lang.Specification

import javax.crypto.AEADBadTagException

class ReferralServiceSpec extends Specification {

    ReferralService referralService

    def setup() {
        referralService = new ReferralService()
    }

    def "encrypt and decrypt should return original inviterId"() {
        given:
        String inviterId = "123456"

        when:
        String encrypted = referralService.encrypt(inviterId)
        String decrypted = referralService.decrypt(encrypted)

        then:
        encrypted != inviterId
        decrypted == inviterId
    }

    def "encrypt and decrypt should return original inviterId even when id is negative"() {
        given:
        String inviterId = "-123456"

        when:
        String encrypted = referralService.encrypt(inviterId)
        String decrypted = referralService.decrypt(encrypted)

        then:
        encrypted != inviterId
        decrypted == inviterId
    }

    def "decrypting an invalid string should throw an exception"() {
        given:
        String invalidEncryptedString = "invalidString"

        when:
        referralService.decrypt(invalidEncryptedString)

        then:
        thrown(IllegalArgumentException)
    }

    def "decrypting a tampered encrypted string should throw AEADBadTagException"() {
        given:
        String inviterId = "123456"
        String encrypted = referralService.encrypt(inviterId)

        byte[] encryptedBytes = Base64.getDecoder().decode(encrypted)
        encryptedBytes[encryptedBytes.length - 1] ^= 1
        String tamperedEncryptedString = Base64.getEncoder().encodeToString(encryptedBytes)

        when:
        referralService.decrypt(tamperedEncryptedString)

        then:
        thrown(AEADBadTagException)
    }
}
