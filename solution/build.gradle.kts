import org.sourcegrade.submitter.submit

plugins {
    id("org.sourcegrade.submitter") version "0.4.0"
}

submit {
    assignmentId = "h10"
    studentId = "ab12cdef"
    firstName = "sol_first"
    lastName = "sol_last"
}

dependencies {
    implementation("org.jetbrains:annotations:20.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}
