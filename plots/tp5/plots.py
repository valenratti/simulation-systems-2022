from matplotlib import pyplot as plt
import numpy as np


def time_vs_sth(labels, times, y_data, y_label, title, legend_title,
                suptitle=None, log_scale=False, zoom=False, ej3=False):  # energy or flow
    # aux = [format(n, '.0e') for n in labels]
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


def mean_and_stdev(x_data, mean_list, stdev_list, title, xlabel, ylabel, beverloo=False, beveerloo_flows=None):
    plt.errorbar(x_data, mean_list, stdev_list, linestyle='None', marker='o')

    if beverloo:
        plt.plot(x_data, beveerloo_flows, linestyle='None', marker='o', color='orange')
        ret = np.polyfit(x_data, beveerloo_flows, 1)
        plt.plot(x_data, np.array(ret[0]) * x_data + ret[1])

    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.title(title)
    plt.xticks(x_data)

    # aux = [format(n, '.0e') for n in x_data]
    # plt.xticks(x_data, aux)
    #
    # y_ticks = [x / 1000 for x in range(1, 8)]
    # aux = [format(n, '.0e') for n in y_ticks]
    # plt.yticks(y_ticks, aux)

    plt.tight_layout()
    plt.show()
