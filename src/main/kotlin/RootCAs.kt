package com.github.OpenICP_BR.ktLib

import java.io.ByteArrayInputStream

fun getRootCert(name: String): Certificate {
    var stream: ByteArrayInputStream
    if (name.toUpperCase() == "V1") {
        stream = ByteArrayInputStream(("-----BEGIN CERTIFICATE-----\n" +
                "MIIEgDCCA2igAwIBAgIBATANBgkqhkiG9w0BAQUFADCBlzELMAkGA1UEBhMCQlIx\n" +
                "EzARBgNVBAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25h\n" +
                "bCBkZSBUZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxNDAyBgNVBAMTK0F1\n" +
                "dG9yaWRhZGUgQ2VydGlmaWNhZG9yYSBSYWl6IEJyYXNpbGVpcmEgdjEwHhcNMDgw\n" +
                "NzI5MTkxNzEwWhcNMjEwNzI5MTkxNzEwWjCBlzELMAkGA1UEBhMCQlIxEzARBgNV\n" +
                "BAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25hbCBkZSBU\n" +
                "ZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxNDAyBgNVBAMTK0F1dG9yaWRh\n" +
                "ZGUgQ2VydGlmaWNhZG9yYSBSYWl6IEJyYXNpbGVpcmEgdjEwggEiMA0GCSqGSIb3\n" +
                "DQEBAQUAA4IBDwAwggEKAoIBAQDOHOi+kzTOybHkVO4J9uykCIWgP8aKxnAwp4CM\n" +
                "7T4BVAeMGSM7n7vHtIsgseL3QRYtXodmurAH3W/RPzzayFkznRWwn5LIVlRYijon\n" +
                "ojQem3i1t83lm+nALhKecHgH+o7yTMD45XJ8HqmpYANXJkfbg3bDzsgSu9H/766z\n" +
                "Yn2aoOS8bn0BLjRg3IfgX38FcFwwFSzCdaM/UANmI2Ys53R3eNtmF9/5Hw2CaI91\n" +
                "h/fpMXpTT89YYrtAojTPwHCEUJcV2iBL6ftMQq0raI6j2a0FYv4IdMTowcyFE86t\n" +
                "KDBQ3d7AgcFJsF4uJjjpYwQzd7WAds0qf/I8rF2TQjn0onNFAgMBAAGjgdQwgdEw\n" +
                "TgYDVR0gBEcwRTBDBgVgTAEBADA6MDgGCCsGAQUFBwIBFixodHRwOi8vYWNyYWl6\n" +
                "LmljcGJyYXNpbC5nb3YuYnIvRFBDYWNyYWl6LnBkZjA/BgNVHR8EODA2MDSgMqAw\n" +
                "hi5odHRwOi8vYWNyYWl6LmljcGJyYXNpbC5nb3YuYnIvTENSYWNyYWl6djEuY3Js\n" +
                "MB0GA1UdDgQWBBRCsixcdAEHvpv/VTM77im7XZG/BjAPBgNVHRMBAf8EBTADAQH/\n" +
                "MA4GA1UdDwEB/wQEAwIBBjANBgkqhkiG9w0BAQUFAAOCAQEAWWyKdukZcVeD/qf0\n" +
                "eg+egdDPBxwMI+kkDVHLM+gqCcN6/w6jgIZgwXCX4MAKVd2kZUyPp0ewV7fzq8TD\n" +
                "GeOY7A2wG1GRydkJ1ulqs+cMsLKSh/uOTRXsEhQZeAxi6hQ5GArFVdtThdx7KPoV\n" +
                "caPKdCWCD2cnNNeuUhMC+8XvmoAlpVKeOQ7tOvR4B1/VKHoKSvXQw2f3jFgXbwoA\n" +
                "oyYQtGAiOkpIpdrgqYTeQ9ufQ6c/KARHki/352R1IdJPgc6qPmQO4w6tVZp+lJs0\n" +
                "wdCuaU4eo9mzh1facMJafYfN+b833u1WNfe3Ig5Pkrg/CN+cnphe8m+5+pss+M1F\n" +
                "2HKyIA==\n" +
                "-----END CERTIFICATE-----").toByteArray())
    } else if (name.toUpperCase() == "V2") {
        stream = ByteArrayInputStream(("-----BEGIN CERTIFICATE-----\n" +
                "MIIGoTCCBImgAwIBAgIBATANBgkqhkiG9w0BAQ0FADCBlzELMAkGA1UEBhMCQlIx\n" +
                "EzARBgNVBAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25h\n" +
                "bCBkZSBUZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxNDAyBgNVBAMTK0F1\n" +
                "dG9yaWRhZGUgQ2VydGlmaWNhZG9yYSBSYWl6IEJyYXNpbGVpcmEgdjIwHhcNMTAw\n" +
                "NjIxMTkwNDU3WhcNMjMwNjIxMTkwNDU3WjCBlzELMAkGA1UEBhMCQlIxEzARBgNV\n" +
                "BAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25hbCBkZSBU\n" +
                "ZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxNDAyBgNVBAMTK0F1dG9yaWRh\n" +
                "ZGUgQ2VydGlmaWNhZG9yYSBSYWl6IEJyYXNpbGVpcmEgdjIwggIiMA0GCSqGSIb3\n" +
                "DQEBAQUAA4ICDwAwggIKAoICAQC6RqQO3edA8rWgfFKVV0X8bYTzhgHJhQOtmKvS\n" +
                "8l4Fmcm7b2Jn/XdEuQMHPNIbAGLUcCxCg3lmq5lWroG8akm983QPYrfrWwdmlEIk\n" +
                "nUasmkIYMPAkqFFB6quV8agrAnhptSknXpwuc8b+I6Xjps79bBtrAFTrAK1POkw8\n" +
                "5wqIW9pemgtW5LVUOB3yCpNkTsNBklMgKs/8dG7U2zM4YuT+jkxYHPePKk3/xZLZ\n" +
                "CVK9z3AAnWmaM2qIh0UhmRZRDTTfgr20aah8fNTd0/IVXEvFWBDqhRnLNiJYKnIM\n" +
                "mpbeys8IUWG/tAUpBiuGkP7pTcMEBUfLz3bZf3Gmh3sVQOQzgHgHHaTyjptAO8ly\n" +
                "UN9pvvAslh+QtdWudONltIwa6Wob+3JcxYJU6uBTB8TMEun33tcv1EgvRz8mYQSx\n" +
                "Epoza7WGSxMr0IadR+1p+/yEEmb4VuUOimx2xGsaesKgWhLRI4lYAXwIWNoVjhXZ\n" +
                "fn03tqRF9QOFzEf6i3lFuGZiM9MmSt4c6dR/5m0muTx9zQ8oCikPm91jq7mmRxqE\n" +
                "14WkA2UGBEtSjYM0Qn8xjhEu5rNnlUB+l3pAAPkRbIM4WK0DM1umxMHFsKwNqQbw\n" +
                "pmkBNLbp+JRITz6mdQnsSsU74MlesDL/n2lZzzwwbw3OJ1fsWhto/+xPb3gyPnnF\n" +
                "tF2VfwIDAQABo4H1MIHyME4GA1UdIARHMEUwQwYFYEwBAQAwOjA4BggrBgEFBQcC\n" +
                "ARYsaHR0cDovL2FjcmFpei5pY3BicmFzaWwuZ292LmJyL0RQQ2FjcmFpei5wZGYw\n" +
                "PwYDVR0fBDgwNjA0oDKgMIYuaHR0cDovL2FjcmFpei5pY3BicmFzaWwuZ292LmJy\n" +
                "L0xDUmFjcmFpenYyLmNybDAfBgNVHSMEGDAWgBQMOSA6twEfy9cofUGgx/pKrTIk\n" +
                "vjAdBgNVHQ4EFgQUDDkgOrcBH8vXKH1BoMf6Sq0yJL4wDwYDVR0TAQH/BAUwAwEB\n" +
                "/zAOBgNVHQ8BAf8EBAMCAQYwDQYJKoZIhvcNAQENBQADggIBAFmaFGkYbX0pQ3B9\n" +
                "dpth33eOGnbkqdbLdqQWDEyUEsaQ0YEDxa0G2S1EvLIJdgmAOWcAGDRtBgrmtRBZ\n" +
                "SLp1YPw/jh0YVXArnkuVrImrCncke2HEx5EmjkYTUTe2jCcK0w3wmisig4OzvYM1\n" +
                "rZs8vHiDKTVhNvgRcTMgVGNTRQHYE1qEO9dmEyS3xEbFIthzJO4cExeWyCXoGx7P\n" +
                "34VQbTzq91CeG5fep2vb1nPSz3xQwLCM5VMSeoY5rDVbZ8fq1PvRwl3qDpdzmK4p\n" +
                "v+Q68wQ2UCzt3h7bhegdhAnu86aDM1tvR3lPSLX8uCYTq6qz9GER+0Vn8x0+bv4q\n" +
                "SyZEGp+xouA82uDkBTp4rPuooU2/XSx3KZDNEx3vBijYtxTzW8jJnqd+MRKKeGLE\n" +
                "0QW8BgJjBCsNid3kXFsygETUQuwq8/JAhzHVPuIKMgwUjdVybQvm/Y3kqPMFjXUX\n" +
                "d5sKufqQkplliDJnQwWOLQsVuzXxYejZZ3ftFuXoAS1rND+Og7P36g9KHj41hJ2M\n" +
                "gDQ/qZXow63EzZ7KFBYsGZ7kNou5uaNCJQc+w+XVaE+gZhyms7ZzHJAaP0C5GlZC\n" +
                "cIf/by0PEf0e//eFMBUO4xcx7ieVzMnpmR6Xx21bB7UFaj3yRd+6gnkkcC6bgh9m\n" +
                "qaVtJ8z2KqLRX4Vv4EadqtKlTlUO\n" +
                "-----END CERTIFICATE-----").toByteArray())
    } else if (name.toUpperCase() == "V5") {
        stream = ByteArrayInputStream(("-----BEGIN CERTIFICATE-----\n" +
                "MIIGoTCCBImgAwIBAgIBATANBgkqhkiG9w0BAQ0FADCBlzELMAkGA1UEBhMCQlIx\n" +
                "EzARBgNVBAoMCklDUC1CcmFzaWwxPTA7BgNVBAsMNEluc3RpdHV0byBOYWNpb25h\n" +
                "bCBkZSBUZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxNDAyBgNVBAMMK0F1\n" +
                "dG9yaWRhZGUgQ2VydGlmaWNhZG9yYSBSYWl6IEJyYXNpbGVpcmEgdjUwHhcNMTYw\n" +
                "MzAyMTMwMTM4WhcNMjkwMzAyMjM1OTM4WjCBlzELMAkGA1UEBhMCQlIxEzARBgNV\n" +
                "BAoMCklDUC1CcmFzaWwxPTA7BgNVBAsMNEluc3RpdHV0byBOYWNpb25hbCBkZSBU\n" +
                "ZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxNDAyBgNVBAMMK0F1dG9yaWRh\n" +
                "ZGUgQ2VydGlmaWNhZG9yYSBSYWl6IEJyYXNpbGVpcmEgdjUwggIiMA0GCSqGSIb3\n" +
                "DQEBAQUAA4ICDwAwggIKAoICAQD3LXgabUWsF+gUXw/6YODeF2XkqEyfk3VehdsI\n" +
                "x+3/ERgdjCS/ouxYR0Epi2hdoMUVJDNf3XQfjAWXJyCoTneHYAl2McMdvoqtLB2i\n" +
                "leQlJiis0fTtYTJayee9BAIdIrCor1Lc0vozXCpDtq5nTwhjIocaZtcuFsdrkl+n\n" +
                "bfYxl5m7vjTkTMS6j8ffjmFzbNPDlJuV3Vy7AzapPVJrMl6UHPXCHMYMzl0KxR/4\n" +
                "7S5XGgmLYkYt8bNCHA3fg07y+Gtvgu+SNhMPwWKIgwhYw+9vErOnavRhOimYo4M2\n" +
                "AwNpNK0OKLI7Im5V094jFp4Ty+mlmfQH00k8nkSUEN+1TGGkhv16c2hukbx9iCfb\n" +
                "mk7im2hGKjQA8eH64VPYoS2qdKbPbd3xDDHN2croYKpy2U2oQTVBSf9hC3o6fKo3\n" +
                "zp0U3dNiw7ZgWKS9UwP31Q0gwgB1orZgLuF+LIppHYwxcTG/AovNWa4sTPukMiX2\n" +
                "L+p7uIHExTZJJU4YoDacQh/mfbPIz3261He4YFmQ35sfw3eKHQSOLyiVfev/n0l/\n" +
                "r308PijEd+d+Hz5RmqIzS8jYXZIeJxym4mEjE1fKpeP56Ea52LlIJ8ZqsJ3xzHWu\n" +
                "3WkAVz4hMqrX6BPMGW2IxOuEUQyIaCBg1lI6QLiPMHvo2/J7gu4YfqRcH6i27W3H\n" +
                "yzamEQIDAQABo4H1MIHyME4GA1UdIARHMEUwQwYFYEwBAQAwOjA4BggrBgEFBQcC\n" +
                "ARYsaHR0cDovL2FjcmFpei5pY3BicmFzaWwuZ292LmJyL0RQQ2FjcmFpei5wZGYw\n" +
                "PwYDVR0fBDgwNjA0oDKgMIYuaHR0cDovL2FjcmFpei5pY3BicmFzaWwuZ292LmJy\n" +
                "L0xDUmFjcmFpenY1LmNybDAfBgNVHSMEGDAWgBRpqL512cTvbOcTReRhbuVo+LZA\n" +
                "XjAdBgNVHQ4EFgQUaai+ddnE72znE0XkYW7laPi2QF4wDwYDVR0TAQH/BAUwAwEB\n" +
                "/zAOBgNVHQ8BAf8EBAMCAQYwDQYJKoZIhvcNAQENBQADggIBABRt2/JiWapef7o/\n" +
                "plhR4PxymlMIp/JeZ5F0BZ1XafmYpl5g6pRokFrIRMFXLyEhlgo51I05InyCc9Td\n" +
                "6UXjlsOASTc/LRavyjB/8NcQjlRYDh6xf7OdP05mFcT/0+6bYRtNgsnUbr10pfsK\n" +
                "/UzyUvQWbumGS57hCZrAZOyd9MzukiF/azAa6JfoZk2nDkEudKOY8tRyTpMmDzN5\n" +
                "fufPSC3v7tSJUqTqo5z7roN/FmckRzGAYyz5XulbOc5/UsAT/tk+KP/clbbqd/hh\n" +
                "evmmdJclLr9qWZZcOgzuFU2YsgProtVu0fFNXGr6KK9fu44pOHajmMsTXK3X7r/P\n" +
                "wh19kFRow5F3RQMUZC6Re0YLfXh+ypnUSCzA+uL4JPtHIGyvkbWiulkustpOKUSV\n" +
                "wBPzvA2sQUOvqdbAR7C8jcHYFJMuK2HZFji7pxcWWab/NKsFcJ3sluDjmhizpQax\n" +
                "bYTfAVXu3q8yd0su/BHHhBpteyHvYyyz0Eb9LUysR2cMtWvfPU6vnoPgYvOGO1Cz\n" +
                "iyGEsgKULkCH4o2Vgl1gQuKWO4V68rFW8a/jvq28sbY+y/Ao0I5ohpnBcQOAawiF\n" +
                "bz6yJtObajYMuztDDP8oY656EuuJXBJhuKAJPI/7WDtgfV8ffOh/iQGQATVMtgDN\n" +
                "0gv8bn5NdUX8UMNX1sHhU3H1UpoW\n" +
                "-----END CERTIFICATE-----").toByteArray())
    } else {
        throw IllegalArgumentException("unknown root certificate: " + name)
    }
    return Certificate(stream)
}
