from matplotlib import pyplot as plt
import numpy as np
import seaborn as sns
from scipy.stats import norm


def plot_solutions(time, beeman, verlet, gpc, analytical_solution):
    plt.plot(time, beeman, label="Beeman")
    plt.plot(time, verlet, label="Verlet")
    plt.plot(time, gpc, label="GPC")
    plt.plot(time, analytical_solution, label="Analítica")

    plt.xlabel('tiempo [s]')
    plt.ylabel('posición [m]')
    # plt.title(f'')

    # zoom in
    plt.xlim(3.1505, 3.1506)
    plt.ylim(0.1046799, 0.104681)
    plt.yscale("log")

    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.show()


def plot_mse(dt, beeman, verlet, gpc):
    plt.plot(dt, beeman, '-o', label="Beeman")
    plt.plot(dt, verlet, '-o', label="Verlet")
    plt.plot(dt, gpc, '-o', label="GPC")

    plt.xlabel('dt [s]')
    plt.ylabel('ECM [$m^2$]')
    # plt.title(f'')

    plt.xscale("log")
    plt.yscale("log")

    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.show()


def percentages_per_v0(v0, left, right, up, down, absorb, dt):
    width = 0.35

    fig, ax = plt.subplots()
    ind = np.arange(len(v0))

    p1 = ax.bar(ind, left, width, label='Izquierda')
    p2 = ax.bar(ind, right, width, bottom=left, label='Derecha')
    p3 = ax.bar(ind, up, width, bottom=left+right, label='Arriba')
    p4 = ax.bar(ind, down, width, bottom=left+right+up, label='Abajo')
    p5 = ax.bar(ind, absorb, width, bottom=left+right+up+down, label='Absorbidas')

    ax.bar_label(p1, label_type='center')
    ax.bar_label(p2, label_type='center')
    ax.bar_label(p3, label_type='center')
    ax.bar_label(p4, label_type='center')
    ax.bar_label(p5, label_type='center')

    plt.xlabel('V0 [m/s]')
    plt.ylabel('Porcentajes')
    aux = [format(n, '.1e') for n in v0]
    plt.xticks(ind, aux)

    plt.title(f'dt = {dt} s')
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.tight_layout()
    plt.show()


def time_vs_energy(dts, times, y_data, y_label, v0):
    for i in range(len(dts)):
        plt.plot(times[i], y_data[i], label=format(dts[i], '.0e'))

    plt.xlabel('tiempo [s]')
    plt.ylabel(y_label)
    v0_s = format(v0, '.0e')
    plt.title(f'v0 = {v0_s} m/s')

    plt.yscale("log")

    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5), title='dt [s]')
    plt.tight_layout()
    plt.show()


def dt_vs_energy(dts, red_avgs, stdevs, y_label, v0):
    plt.errorbar(dts, red_avgs, yerr=stdevs, linestyle='None', marker='o')

    plt.xlabel('dt [s]')
    plt.ylabel(y_label)
    v0_s = format(v0, '.0e')
    plt.title(f'v0 = {v0_s} m/s')

    plt.xscale("log")
    plt.yscale("log")

    plt.tight_layout()
    plt.show()


def v0_vs_len(v0s, lens, stdevs, y_label, dt):
    plt.errorbar(v0s, lens, yerr=stdevs, linestyle='None', marker='o')

    plt.xlabel('v0 [m/s]')
    plt.ylabel(y_label)
    plt.title(f'dt = {dt} s')

    # plt.xscale("log")
    plt.yscale("log")

    plt.tight_layout()
    plt.show()


def trajectory(x_list, y_list, v0, dt):
    plt.plot(x_list, y_list)
    plt.xlabel('x [m]')
    plt.ylabel('y [m]')
    plt.suptitle('Trayectoria de la partícula')
    plt.title(f'v0 = {v0} m/s, dt = {dt} s')

    plt.tight_layout()
    plt.show()


# Probability Density Function
def trajectory_len_pdf(v0s, data_list):
    for i in range(len(v0s)):
        # aux = sorted(data_list[i])
        # plt.plot(aux, norm.pdf(aux))
        sns.distplot(data_list[i], hist=False, kde=True,
                     kde_kws={'linewidth': 1}, label=str(v0s[i]))
    plt.xscale("log")
    # plt.yscale("symlog")

    plt.xlabel('longitud de la trayectoria [m]')
    plt.ylabel('densidad')

    plt.title(f'dt = {1e-15} s')
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5), title='v0 [m/s]')
    plt.tight_layout()
    plt.show()
