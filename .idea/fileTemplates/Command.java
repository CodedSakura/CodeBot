#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end
#parse("File Header.java")
import codebot.Command;

class ${NAME} extends Command {
    ${NAME}() {
        name = "${name}";
        info = "${info}";
    }
}