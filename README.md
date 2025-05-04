# Striker Java Simulator

This README describes how to build, run, test, install, and manage simulations using the Java version of the Striker Blackjack simulator. It uses a `Makefile` integrated with Gradle to automate all tasks.

---

## 🚀 Build the Project

```sh
make build
```

This command uses Gradle to compile the Java project and build the required JAR file.

---

## 🧪 Run the Simulator

```sh
make run STRATEGY=mimic DECKS=single-deck HANDS=500000000 THREADS=24
```

This command runs the simulator with the given strategy, deck type, number of hands, and number of threads. Output is logged to:

```
${HOME}/Striker/Simulations/YYYY/MM/DD/striker-java-HHMMSS.log
```

All combinations can be run via:

```sh
make run-all
```

---

## 🧹 Clean the Build

```sh
make clean
```

This removes all generated class files and the JAR.

---

## 🧼 Format Java Files

```sh
make format
```

This command formats all `.java` files using `jfs` (Java Formatter Shell).

---

## 🧪 Run Tests

```sh
make test
```

Executes all unit and integration tests via Gradle.

---

## ✅ Lint the Code

```sh
make lint
```

Runs Gradle's `check` task for static analysis and style checks.

---

## 🧊 Compile to Native Binary

```sh
make compile
```

This compiles the Java simulator into a native binary using `GraalVM` and stores it at:

```
bin/striker-java
```

---

## 📦 Install the Binary

```sh
make install
```

Installs the compiled binary to:

```
${HOME}/Striker/bin/
```

---

## 🧠 Strategies and Decks

Available strategies:

- `mimic`
- `linear`
- `polynomial`
- `neural`
- `basic`
- `high-low`
- `wong`

Available deck types:

- `single-deck`
- `double-deck`
- `six-shoe`

---

## 🎯 Targeted Runs

Each strategy/deck combination has its own make target:

```sh
make run-mimic-single-deck
make run-neural-six-shoe
make run-linear-double-deck
```

You can also run all decks for a single strategy:

```sh
make run-mimic
make run-polynomial
```

---

## 🔁 Aliases for Quick Access

Shortcut targets for frequently used combinations:

- `r1`: All strategies with single-deck
- `r2`: All strategies with double-deck
- `r6`: All strategies with six-shoe

- `rm`: mimic
  - `rm1`, `rm2`, `rm6`: mimic with each deck
- `rl`: linear
  - `rl1`, `rl2`, `rl6`: linear with each deck
- `rp`: polynomial
- `rn`: neural
- `rb`: basic
- `rh`: high-low
- `rw`: wong

Example:

```sh
make rl1   # run linear strategy on single-deck
make rw6   # run wong strategy on six-shoe
```

---

## 🖥️ Machine Restriction Logic

Only runs fully if the local machine matches the expected hostname. Otherwise, you’ll see:

```java
if (!isMyComputer()) {
    System.out.println("This code is restricted to running only on my computer.");
    return;
}
```

To update allowed host:

```java
String myHostname = "your-hostname";
```

---

## 📄 Sample Command

```sh
make run STRATEGY=neural DECKS=six-shoe HANDS=10000000 THREADS=16
```

This will run the simulation and log results appropriately.
