from matplotlib import pyplot as plt
import numpy as np
import seaborn as sns


def time_vs_sth(Ds, times, y_data, y_label, suptitle, title, log_scale=False):  # energy or flow
    # TODO con todos los Ds en el mismo plot
    for i in range(len(Ds)):
        plt.plot(times[i], y_data[i], label=Ds[i])

    plt.xlabel('tiempo [s]')
    plt.ylabel(y_label)
    plt.suptitle(suptitle)
    plt.title(title)

    if log_scale:
        plt.yscale("log")   # FIXME: log o semlog?

    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5), title='Apertura D [m]')
    plt.tight_layout()
    plt.show()


def mean_and_stdev(Ds, mean_list, stdev_list):
    plt.errorbar(Ds, mean_list, stdev_list, linestyle='None', marker='o')

    plt.xlabel('Apertura D [m]')
    # plt.ylabel('Caudal')
    plt.title('Valor medio y desviación estándar del caudal')
    plt.xticks(Ds)

    plt.tight_layout()
    plt.show()


def residual_energy_vs_friction_param():
    # TODO
    print("plot sth")

    # TODO: barra de error

    plt.xlabel('energía residual promedio')
    plt.ylabel('parámetro de fricción')
    plt.title(f'Energía residual en función del parámetro de fricción')
    # TODO: t desde cuando se empieza a promediar?

    plt.tight_layout()
    plt.show()
