package protocol.buffered

import protocol.typehandle.TypeHandler
import protocol.Primitive
import protocol.typehandle.TypeHandleCaller
import protocol.typehandle.TypeHandleCallerImpl
import java.util.*

abstract class ProtocolBufferReader(var protocolBuffer: ProtocolBuffer)
    : ProtocolReader, TypeHandleCaller {

    val typeHandleDelegator: TypeHandleCaller =
        TypeHandleCallerImpl()

    override val typeHandlerTable: Hashtable<Class<*>, TypeHandler<*>?>
        get() = typeHandleDelegator.typeHandlerTable

    init {
        onHandlerSetup()
    }

    abstract fun onHandlerSetup()

    override fun read() {
        val pbuffer = protocolBuffer
        val cIndex = pbuffer.currentComponentIndex()

        pbuffer.allocComponentBuffer()
        while (pbuffer.hasComponentBytesRemaining())
            callHandler(pbuffer.get(), cIndex)

        pbuffer.headToNextComponent()
    }

    override fun hasRemaining(): Boolean {
        return protocolBuffer.hasBytesRemaining()
    }

    override fun <T : Any> callHandler(typedData: T, handlingHint: Int) {
        typeHandleDelegator.callHandler(typedData, handlingHint)
    }

    override fun <K : Class<*>> addHandler(targetType: K, handler: TypeHandler<*>) {
        typeHandleDelegator.addHandler(targetType, handler)
    }

    protected fun addByteHandler(handler: TypeHandler<Byte>) {
        addHandler(Primitive.Byte, handler)
    }

    protected fun addCharHandler(handler: TypeHandler<Char>) {
        addHandler(Primitive.Char, handler)
    }

    protected fun addShortHandler(handler: TypeHandler<Short>) {
        addHandler(Primitive.Short, handler)
    }

    protected fun addIntHandler(handler: TypeHandler<Int>) {
        addHandler(Primitive.Int, handler)
    }

    protected fun addFloatHandler(handler: TypeHandler<Float>) {
        addHandler(Primitive.Float, handler)
    }

    protected fun addDoubleHandler(handler: TypeHandler<Double>) {
        addHandler(Primitive.Double, handler)
    }
}