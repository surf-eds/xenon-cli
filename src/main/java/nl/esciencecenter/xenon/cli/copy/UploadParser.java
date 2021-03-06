package nl.esciencecenter.xenon.cli.copy;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

import static nl.esciencecenter.xenon.cli.ParserHelpers.addCopyModeArguments;

public class UploadParser extends CopyCommand {
    public Subparser buildArgumentParser(Subparsers subparsers) {
        Subparser subparser = subparsers.addParser("upload")
            .setDefault("command", new UploadCommand())
            .defaultHelp(true)
            .help("Upload local file to target")
            .description("Upload local file to target");
        subparser.addArgument("source")
            .type(Arguments.fileType().acceptSystemIn().verifyCanRead())
            .help("Source path at location, use '-' for stdin")
            .required(true);
        subparser.addArgument("target").help("Target path").required(true);
        subparser.addArgument("--recursive").help("Upload directories recursively").action(Arguments.storeTrue());
        addCopyModeArguments(subparser);
        return subparser;
    }
}
