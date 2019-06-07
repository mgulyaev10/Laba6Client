package collection;

import network.TransferPackage;

import java.io.UnsupportedEncodingException;
import java.util.stream.Stream;

interface Startable {
    void start(Command command, TransferPackage transferPackage, Stream data) throws UnsupportedEncodingException;
}
