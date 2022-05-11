from matplotlib import pyplot as plt


def plot_solutions(time, beeman, verlet, gpc, analytical_solution):
    plt.plot(time, beeman, label="Beeman")
    plt.plot(time, verlet, label="Verlet")
    plt.plot(time, gpc, label="GPC")
    plt.plot(time, analytical_solution, label="Analítica")

    plt.xlabel('tiempo [s]')
    plt.ylabel('posición [m]')
    # plt.title(f'')

    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.show()


def plot_mse(dt, beeman, verlet, gpc):
    plt.plot(dt, beeman, 'o', label="Beeman")
    plt.plot(dt, verlet, 'o', label="Verlet")
    plt.plot(dt, gpc, 'o', label="GPC")

    plt.xlabel('dt [s]')
    plt.ylabel('ECM [$m^2$]')
    # plt.title(f'')

    plt.xscale("log")
    plt.yscale("log")

    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.show()
