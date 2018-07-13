class Square() {
    private var on = false

    fun isOn(): Boolean {
        return on
    }

    fun flip() {
        on = !on
    }
}