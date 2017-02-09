package nl.esciencecenter.xenon.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.esciencecenter.xenon.XenonException;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class MainTest {
    @Rule
    public TemporaryFolder myfolder = new TemporaryFolder();

    @Test
    public void buildXenonProperties() throws Exception {
        Map<String, Object> attrs = new HashMap<>();
        List<String> propsIn = Arrays.asList("KEY1=VAL1", "KEY2=VAL2");
        attrs.put("props", propsIn);
        Namespace ns = new Namespace(attrs);

        Map<String, String> result = Main.buildXenonProperties(ns);
        Map<String, String> expected = new HashMap<>();
        expected.put("KEY1", "VAL1");
        expected.put("KEY2", "VAL2");
        assertEquals(expected, result);
    }

    @Test(expected = ArgumentParserException.class)
    public void mainRootHelp() throws XenonException, ArgumentParserException {
        String[] args = {"--help"};
        Main main = new Main();
        main.run(args);
    }

    @Test
    public void copyLocalFile() throws XenonException, ArgumentParserException, IOException {
        File target = myfolder.newFile("copy-of-README.md");
        String[] args = {"file", "copy", "--overwrite", "README.md", target.getPath()};
        Main main = new Main();
        main.run(args);
        assertTrue(target.isFile());
    }

    @Test
    public void listLocal() throws IOException, XenonException, ArgumentParserException {
        myfolder.newFile("file1").createNewFile();
        myfolder.newFile(".hidden1").createNewFile();
        File dir1 = myfolder.newFolder("dir1");
        dir1.mkdirs();
        new File(dir1, "file3").createNewFile();
        File hdir2 = myfolder.newFolder(".hidden2");
        hdir2.mkdirs();

        String path = myfolder.getRoot().getCanonicalPath();
        String[] args = {"file", "list", path};
        Main main = new Main();
        ListFilesOutput output = (ListFilesOutput) main.run(args);

        ListFilesOutput expected = new ListFilesOutput();
        expected.objects.add("file1");
        expected.objects.add("dir1");
        expected.directories.add("dir1");
        expected.files.add("file1");
        assertEquals(expected, output);
    }
}