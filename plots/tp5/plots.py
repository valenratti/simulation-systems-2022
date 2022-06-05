from matplotlib import pyplot as plt
import numpy as np


def time_vs_sth(labels, times, y_data, y_label, title, legend_title, suptitle=None, log_scale=False, zoom=False):  # energy or flow
    for i in range(len(labels)):
        plt.plot(times[i], y_data[i], label=labels[i])

    plt.xlabel('Tiempo [s]')
    plt.ylabel(y_label)
    plt.suptitle(suptitle)
    plt.title(title)

    if zoom:
        plt.ylim(1e-1, 1e1)

    if log_scale:
        plt.yscale("symlog")   # FIXME: log o symlog?

    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5), title=legend_title)
    plt.tight_layout()
    plt.show()


def mean_and_stdev(Ds, mean_list, stdev_list, title, beverloo=False, beveerloo_flows=None):
    plt.errorbar(Ds, mean_list, stdev_list, linestyle='None', marker='o')

    if beverloo:
        plt.plot(Ds, beveerloo_flows, linestyle='None', marker='o')
        ret = np.polyfit(Ds, beveerloo_flows, 1)
        plt.plot(Ds, np.array(ret[0]) * Ds + ret[1])

    plt.xlabel('Apertura D [m]')
    plt.ylabel('Caudal [partículas/s]')
    plt.title(title)
    plt.xticks(Ds)

    plt.tight_layout()
    plt.show()


def residual_energy_vs_friction_param():
    # TODO
    print("plot sth")

    # TODO: barra de error

    plt.xlabel('Energía residual promedio') # TODO: unidades
    plt.ylabel('Parámetro de fricción [N/m]')
    plt.title(f'Energía residual en función del parámetro de fricción')
    # TODO: t desde cuando se empieza a promediar?

    plt.tight_layout()
    plt.show()
