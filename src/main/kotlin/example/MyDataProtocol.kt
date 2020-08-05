package example

import protocol.BufferDataProtocol
import protocol.buffer.BufferDescriptor
import protocol.data.ByteHandler
import protocol.data.ShortHandler
import java.nio.ByteBuffer

class MyDataProtocol(buffer: ByteBuffer, descriptor: BufferDescriptor)
    : BufferDataProtocol(buffer, descriptor) {

    var changeSize = 3
    override fun setup() {
        super.setup()

        addByteHandler(object: ByteHandler {
            override fun handle(data: Byte, hintValue: Int) {
                if (hintValue == 0) {
                    println("this is first header")
                    bufferDescriptor.changeComponentNumber(1, changeSize++)
                } else {
                    println("this is second header")
                }
            }
        })

        addShortHandler(object: ShortHandler {
            override fun handle(data: Short, hintValue: Int) {
                println("this is short")
            }
        })
    }
}