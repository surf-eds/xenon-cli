package nl.esciencecenter.xenon.cli.sftp;

import static org.junit.Assert.assertFalse;

import com.github.geowarin.junit.DockerRule;
import nl.esciencecenter.xenon.cli.copy.CopyInput;
import nl.esciencecenter.xenon.credentials.PasswordCredential;
import org.junit.ClassRule;
import org.junit.Test;

public class CopyInputTest {
    private static final String PORT = "22/tcp";

    @ClassRule
    public static final DockerRule server = DockerRule.builder()
            .image("nlesc/xenon-ssh")
            .ports("22")
            .waitForPort(PORT)
            .build();

    private static String getLocation() {
        return server.getDockerHost() + ":" + server.getHostPort(PORT);
    }

    @Test
    public void isStream_sshdash_false() throws Exception {
        PasswordCredential cred = new PasswordCredential("xenon", "javagat".toCharArray());
        CopyInput copyInput = new CopyInput("sftp", getLocation(), "-", cred);
        copyInput.getFileSystem().close();
        assertFalse(copyInput.isStream());
    }
}