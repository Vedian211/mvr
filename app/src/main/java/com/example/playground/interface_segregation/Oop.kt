package com.example.playground.interface_segregation

fun main() {
    val d = D.Base()
    val e = E.Base()
    val f = F.Base()
    val g = G.Base()

    val save: DataSource.Save = provideSave(d, e, f)
    val read: DataSource.Read = provideRead(f, g)
    val mutable: DataSource.Mutable = provideMutable(d, e, f, g)

    mutable.save()
    mutable.fetch()

    // interface segregation - если нам нужно использовать только часть функционала мы можем создать несколько интерфейсов
    // и разделить логику по интерфейсам. В таком случае в реализациях будут доступны только те функции которые нам нужны
    // Например interface Save имеет доступ только к сохранению, а interface Read только к чтению
    save.save()
    read.fetch()
}

fun provideSave(d: D, e: E, f: F): DataSource.Save = DataSource.Save.Base(d, e, f)
fun provideRead(f: F, g: G): DataSource.Read = DataSource.Read.Base(f, g)
fun provideMutable(d: D, e: E, f: F, g: G): DataSource.Mutable =
    DataSource.Base(provideSave(d, e, f), provideRead(f, g))

interface DataSource {

    interface Save {
        fun save()
        class Base(
            private val d: D,
            private val e: E,
            private val f: F
        ): Save {
            override fun save() {
                d.d()
                e.e()
                f.f()
            }
        }
    }

    interface Read {
        fun fetch()
        class Base(
            private val f: F,
            private val g: G
        ): Read {
            override fun fetch() {
                f.f()
                g.g()
            }
        }
    }

    interface Mutable: Save, Read

    class Base(
        private val save: Save,
        private val read: Read
    ): Mutable {
        override fun fetch() {
            read.fetch()
        }

        override fun save() {
            save.save()
        }
    }
}

interface D {
    fun d()
    class Base: D {
        override fun d() {
            println("D.d")
        }
    }
}

interface E {
    fun e()
    class Base: E {
        override fun e() {
            println("E.e")
        }
    }
}

interface F {
    fun f()
    class Base: F {
        override fun f() {
            println("F.f")
        }
    }
}

interface G {
    fun g()
    class Base: G {
        override fun g() {
            println("G.g")
        }
    }
}