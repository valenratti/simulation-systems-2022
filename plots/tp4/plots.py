from matplotlib import pyplot as plt
import numpy as np


def plot_solutions(time, beeman, verlet, gpc, analytical_solution):
    plt.plot(time, beeman, label="Beeman")
    plt.plot(time, verlet, label="Verlet")
    plt.plot(time, gpc, label="GPC")
    plt.plot(time, analytical_solution, label="Analítica")

    plt.xlabel('tiempo [s]')
    plt.ylabel('posición [m]')
    # plt.title(f'')

    # plt.xlim(3.15059, 3.1506)
    # plt.ylim(0.1046, 0.10469)

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


def percentages_per_v0(v0, left, right, up, down, absorb):
    width = 0.35

    fig = plt.figure()
    ind = np.arange(len(v0))

    p1 = plt.bar(ind, left, width, label='Izquierda')
    p2 = plt.bar(ind, right, width, bottom=left, label='Derecha')
    p3 = plt.bar(ind, up, width, bottom=left+right, label='Arriba')
    p4 = plt.bar(ind, down, width, bottom=left+right+up, label='Abajo')
    p5 = plt.bar(ind, absorb, width, bottom=left+right+up+down, label='Absorbidas')

    plt.xlabel('V0 [m/s]')
    plt.ylabel('Porcentajes')
    aux = [format(n, '.0e') for n in v0]
    plt.xticks(ind, aux)

    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.show()
